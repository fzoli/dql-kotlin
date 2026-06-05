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

private const val ANNOTATION_FQN = "com.farcsal.dql.query.parser.annotation.GenerateDqlFilterResolver"
private const val FIELD_FQN = "com.farcsal.query.api.Field"
private const val SET_FIELD_FQN = "com.farcsal.query.api.SetField"
private const val SERIALIZED_FIELD_FQN = "com.farcsal.query.api.SerializedField"

internal class DqlFilterResolverProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(ANNOTATION_FQN)
        val deferred = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { processClass(it as KSClassDeclaration) }
        return deferred
    }

    private fun processClass(classDecl: KSClassDeclaration) {
        val packageName = classDecl.packageName.asString()
        val className = classDecl.simpleName.asString()
        val resolverClassName = "${className}DqlFilterFieldExpressionResolver"
        val parserClassName = "${className}DqlFilterFieldParser"

        val params = classDecl.primaryConstructor?.parameters ?: run {
            logger.error("$className: no primary constructor", classDecl)
            return
        }
        val propsByName = classDecl.getAllProperties().associateBy { it.simpleName.asString() }

        val properties = params.mapNotNull { param ->
            val name = param.name?.asString() ?: return@mapNotNull null
            val prop = propsByName[name]
            val resolvedType = param.type.resolve().resolveTypeAliases()
            val serializedName = findSerializedName(param, prop) ?: name
            val isField = resolvedType.implementsInterface(FIELD_FQN)
            val setFieldInner = if (isField) resolvedType.setFieldInnerIfNonField() else null
            val setFieldLeafInner = if (isField) resolvedType.setFieldInnerIfField() else null
            PropertyInfo(name, serializedName, resolvedType, isField, setFieldInner, setFieldLeafInner)
        }

        generateResolver(packageName, className, resolverClassName, properties)
        generateParser(packageName, className, parserClassName, resolverClassName)
    }

    private fun findSerializedName(param: KSValueParameter, prop: KSPropertyDeclaration?): String? {
        param.annotations.findSerializedField()?.let { return it }
        prop?.annotations?.findSerializedField()?.let { return it }
        return null
    }

    private fun Sequence<KSAnnotation>.findSerializedField(): String? =
        find { it.annotationType.resolve().declaration.qualifiedName?.asString() == SERIALIZED_FIELD_FQN }
            ?.arguments
            ?.find { it.name?.asString() == "name" || it.name == null }
            ?.value as? String

    private fun KSType.resolveTypeAliases(): KSType {
        var current = this
        while (true) {
            val decl = current.declaration
            current = if (decl is KSTypeAlias) decl.type.resolve() else return current
        }
    }

    private fun KSType.implementsInterface(fqn: String): Boolean {
        val decl = declaration as? KSClassDeclaration ?: return false
        if (decl.qualifiedName?.asString() == fqn) return true
        return decl.getAllSuperTypes().any { it.declaration.qualifiedName?.asString() == fqn }
    }

    private fun KSType.setFieldInnerIfNonField(): KSType? {
        if (!implementsInterface(SET_FIELD_FQN)) return null
        val inner = resolveSetFieldTypeArg() ?: return null
        return if (inner.implementsInterface(FIELD_FQN)) null else inner
    }

    private fun KSType.setFieldInnerIfField(): KSType? {
        if (!implementsInterface(SET_FIELD_FQN)) return null
        val inner = resolveSetFieldTypeArg() ?: return null
        return if (inner.implementsInterface(FIELD_FQN)) inner else null
    }

    private fun KSType.resolveSetFieldTypeArg(): KSType? {
        val decl = declaration as? KSClassDeclaration ?: return null
        if (decl.qualifiedName?.asString() == SET_FIELD_FQN) {
            return arguments.firstOrNull()?.type?.resolve()
        }
        val setFieldSuperType = decl.getAllSuperTypes()
            .find { it.declaration.qualifiedName?.asString() == SET_FIELD_FQN }
            ?: return null
        return setFieldSuperType.arguments.firstOrNull()?.type?.resolve()
    }

    private fun generateResolver(
        packageName: String,
        className: String,
        resolverClassName: String,
        properties: List<PropertyInfo>,
    ) {
        val nestedProps = properties.filter { !it.isFieldType }
        val setFieldProps = properties.filter { it.setFieldInnerType != null }
        val setFieldLeafProps = properties.filter { it.setFieldLeafInner != null }
        val leafProps = properties.filter { it.isFieldType && it.setFieldInnerType == null && it.setFieldLeafInner == null }

        val imports = mutableListOf(
            "com.farcsal.dql.query.parser.filter.resolver.DqlFilterFieldExpressionResolver",
            "com.farcsal.dql.query.parser.util.unknownFieldException",
            "com.farcsal.query.api.Field",
        )
        if (nestedProps.isNotEmpty() || setFieldProps.isNotEmpty() || setFieldLeafProps.isNotEmpty()) {
            imports += "com.farcsal.dql.query.parser.util.isEmbeddedField"
            imports += "com.farcsal.dql.query.parser.util.asEmbeddedField"
        }
        if (setFieldProps.isNotEmpty() || setFieldLeafProps.isNotEmpty()) {
            imports += "com.farcsal.query.api.SetFieldMethods"
        }
        for (prop in nestedProps) {
            val nestedPkg = prop.type.declaration.packageName.asString()
            if (nestedPkg != packageName) {
                imports += "$nestedPkg.${prop.type.declaration.simpleName.asString()}DqlFilterFieldExpressionResolver"
            }
        }
        for (prop in setFieldProps) {
            val innerPkg = prop.setFieldInnerType!!.declaration.packageName.asString()
            if (innerPkg != packageName) {
                imports += "$innerPkg.${prop.setFieldInnerType.declaration.simpleName.asString()}DqlFilterFieldExpressionResolver"
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
            writer.println("class $resolverClassName(private val f: $className) : DqlFilterFieldExpressionResolver {")

            for (prop in nestedProps) {
                val nestedType = prop.type.declaration.simpleName.asString()
                writer.println("    private val ${prop.propName}Resolver by lazy { ${nestedType}DqlFilterFieldExpressionResolver(f.${prop.propName}) }")
            }
            for (prop in setFieldProps) {
                val innerType = prop.setFieldInnerType!!.declaration.simpleName.asString()
                writer.println("    private val ${prop.propName}SetResolver by lazy { ${innerType}DqlFilterFieldExpressionResolver(f.${prop.propName}.oneOf) }")
            }
            if (nestedProps.isNotEmpty() || setFieldProps.isNotEmpty() || setFieldLeafProps.isNotEmpty()) writer.println()

            if (nestedProps.isEmpty() && setFieldProps.isEmpty() && setFieldLeafProps.isEmpty()) {
                writer.println("    override fun getExpression(field: String): Field = when (field) {")
                for (prop in leafProps) {
                    writer.println("        \"${prop.serializedName}\" -> f.${prop.propName}")
                }
                writer.println("        else -> throw unknownFieldException(field)")
                writer.println("    }")
            } else {
                writer.println("    override fun getExpression(field: String): Field {")
                for (prop in nestedProps) {
                    writer.println("        if (isEmbeddedField(field, \"${prop.serializedName}\")) {")
                    writer.println("            return ${prop.propName}Resolver.getExpression(asEmbeddedField(field, \"${prop.serializedName}\"))")
                    writer.println("        }")
                }
                for (prop in setFieldProps) {
                    writer.println("        if (isEmbeddedField(field, \"${prop.serializedName}\")) {")
                    writer.println("            val sub = asEmbeddedField(field, \"${prop.serializedName}\")")
                    writer.println("            if (isEmbeddedField(sub, SetFieldMethods.ONE_OF)) {")
                    writer.println("                return ${prop.propName}SetResolver.getExpression(asEmbeddedField(sub, SetFieldMethods.ONE_OF))")
                    writer.println("            }")
                    writer.println("        }")
                }
                for (prop in setFieldLeafProps) {
                    writer.println("        if (isEmbeddedField(field, \"${prop.serializedName}\")) {")
                    writer.println("            val sub = asEmbeddedField(field, \"${prop.serializedName}\")")
                    writer.println("            if (isEmbeddedField(sub, SetFieldMethods.ONE_OF)) {")
                    writer.println("                return f.${prop.propName}.oneOf")
                    writer.println("            }")
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

    private fun generateParser(
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
            writer.println("import com.farcsal.dql.query.parser.filter.DqlFilterFactory")
            writer.println("import com.farcsal.dql.query.parser.filter.field.DqlFilterFieldParser")
            writer.println("import com.farcsal.query.api.filter.FilterFunction")
            writer.println()
            writer.println("class $parserClassName(private val factory: DqlFilterFactory) : DqlFilterFieldParser<$className> {")
            writer.println("    override fun parseFilter(filter: String?): FilterFunction<$className> =")
            writer.println("        { factory.create(filter, $resolverClassName(this)) }")
            writer.println("}")
        }
    }
}

private data class PropertyInfo(
    val propName: String,
    val serializedName: String,
    val type: KSType,
    val isFieldType: Boolean,
    val setFieldInnerType: KSType?,   // T when SetField<T> and T is non-Field (needs nested resolver)
    val setFieldLeafInner: KSType?,   // T when SetField<T> and T IS a Field (returns f.xxx.oneOf directly)
)
