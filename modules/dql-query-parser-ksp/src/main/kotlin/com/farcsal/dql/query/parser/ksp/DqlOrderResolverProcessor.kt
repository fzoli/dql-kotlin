/*
 * Copyright 2022 Zoltan Farkas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.farcsal.dql.query.parser.ksp

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.validate
import java.io.PrintWriter

private const val ORDER_ANNOTATION_FQN = "com.farcsal.dql.query.parser.annotation.GenerateDqlOrderResolver"
private const val ORDER_FIELD_FQN = "com.farcsal.query.api.OrderField"
private const val ORDER_SERIALIZED_FIELD_FQN = "com.farcsal.query.api.SerializedField"

internal class DqlOrderResolverProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(ORDER_ANNOTATION_FQN)
        val deferred = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { processClass(it as KSClassDeclaration) }
        return deferred
    }

    private fun processClass(classDecl: KSClassDeclaration) {
        val packageName = classDecl.packageName.asString()
        val className = classDecl.simpleName.asString()
        val resolverClassName = "${className}DqlOrderFieldExpressionResolver"
        val parserClassName = "${className}DqlOrderFieldParser"

        val params = classDecl.primaryConstructor?.parameters ?: run {
            logger.error("$className: no primary constructor", classDecl)
            return
        }
        val propsByName = classDecl.getAllProperties().associateBy { it.simpleName.asString() }

        val properties = params.mapNotNull { param ->
            val name = param.name?.asString() ?: return@mapNotNull null
            val prop = propsByName[name]
            val resolvedType = param.type.resolve().resolveOrderTypeAliases()
            val serializedName = findOrderSerializedName(param, prop) ?: name
            val isOrderField = resolvedType.implementsOrderInterface(ORDER_FIELD_FQN)
            OrderPropertyInfo(name, serializedName, resolvedType, isOrderField)
        }

        generateOrderResolver(packageName, className, resolverClassName, properties)
        generateOrderParser(packageName, className, parserClassName, resolverClassName)
    }

    private fun findOrderSerializedName(param: KSValueParameter, prop: KSPropertyDeclaration?): String? {
        param.annotations.findOrderSerializedField()?.let { return it }
        prop?.annotations?.findOrderSerializedField()?.let { return it }
        return null
    }

    private fun Sequence<KSAnnotation>.findOrderSerializedField(): String? =
        find { it.annotationType.resolve().declaration.qualifiedName?.asString() == ORDER_SERIALIZED_FIELD_FQN }
            ?.arguments
            ?.find { it.name?.asString() == "name" || it.name == null }
            ?.value as? String

    private fun KSType.resolveOrderTypeAliases(): KSType {
        var current = this
        while (true) {
            val decl = current.declaration
            current = if (decl is KSTypeAlias) decl.type.resolve() else return current
        }
    }

    private fun KSType.implementsOrderInterface(fqn: String): Boolean {
        val decl = declaration as? KSClassDeclaration ?: return false
        if (decl.qualifiedName?.asString() == fqn) return true
        return decl.getAllSuperTypes().any { it.declaration.qualifiedName?.asString() == fqn }
    }

    private fun generateOrderResolver(
        packageName: String,
        className: String,
        resolverClassName: String,
        properties: List<OrderPropertyInfo>,
    ) {
        val nestedProps = properties.filter { !it.isOrderFieldType }
        val leafProps = properties.filter { it.isOrderFieldType }

        val imports = mutableListOf(
            "com.farcsal.dql.query.parser.order.resolver.DqlOrderFieldExpressionResolver",
            "com.farcsal.dql.query.parser.util.unknownFieldException",
            "com.farcsal.query.api.OrderField",
        )
        if (nestedProps.isNotEmpty()) {
            imports += "com.farcsal.dql.query.parser.util.isEmbeddedField"
            imports += "com.farcsal.dql.query.parser.util.asEmbeddedField"
        }
        for (prop in nestedProps) {
            val nestedPkg = prop.type.declaration.packageName.asString()
            if (nestedPkg != packageName) {
                imports += "$nestedPkg.${prop.type.declaration.simpleName.asString()}DqlOrderFieldExpressionResolver"
            }
        }

        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(false),
            packageName = packageName,
            fileName = resolverClassName,
        )
        PrintWriter(file).use { writer ->
            writer.println("package $packageName")
            writer.println()
            imports.sorted().forEach { writer.println("import $it") }
            writer.println()
            writer.println("class $resolverClassName(private val f: $className) : DqlOrderFieldExpressionResolver {")

            for (prop in nestedProps) {
                val nestedType = prop.type.declaration.simpleName.asString()
                writer.println("    private val ${prop.propName}Resolver by lazy { ${nestedType}DqlOrderFieldExpressionResolver(f.${prop.propName}) }")
            }
            if (nestedProps.isNotEmpty()) writer.println()

            if (nestedProps.isEmpty()) {
                writer.println("    override fun getExpression(field: String): OrderField = when (field) {")
                for (prop in leafProps) {
                    writer.println("        \"${prop.serializedName}\" -> f.${prop.propName}")
                }
                writer.println("        else -> throw unknownFieldException(field)")
                writer.println("    }")
            } else {
                writer.println("    override fun getExpression(field: String): OrderField {")
                for (prop in nestedProps) {
                    writer.println("        if (isEmbeddedField(field, \"${prop.serializedName}\")) {")
                    writer.println("            return ${prop.propName}Resolver.getExpression(asEmbeddedField(field, \"${prop.serializedName}\"))")
                    writer.println("        }")
                }
                writer.println("        return when (field) {")
                for (prop in leafProps) {
                    writer.println("            \"${prop.serializedName}\" -> f.${prop.propName}")
                }
                writer.println("            else -> throw unknownFieldException(field)")
                writer.println("        }")
                writer.println("    }")
            }

            writer.println("}")
        }
    }

    private fun generateOrderParser(
        packageName: String,
        className: String,
        parserClassName: String,
        resolverClassName: String,
    ) {
        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(false),
            packageName = packageName,
            fileName = parserClassName,
        )
        PrintWriter(file).use { writer ->
            writer.println("package $packageName")
            writer.println()
            writer.println("import com.farcsal.dql.query.parser.order.OrderFunctionFactory")
            writer.println("import com.farcsal.dql.query.parser.order.field.DqlOrderFieldParser")
            writer.println("import com.farcsal.query.api.order.OrderFunction")
            writer.println()
            writer.println("class $parserClassName(private val factory: OrderFunctionFactory) : DqlOrderFieldParser<$className> {")
            writer.println("    override fun parseOrder(order: String?): OrderFunction<$className> =")
            writer.println("        factory.create(order) { $resolverClassName(this) }")
            writer.println("}")
        }
    }
}

private data class OrderPropertyInfo(
    val propName: String,
    val serializedName: String,
    val type: KSType,
    val isOrderFieldType: Boolean,
)
