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
package com.farcsal.dql.resolver

import com.farcsal.datatype.stack.DequeStack
import com.farcsal.datatype.stack.Stack
import com.farcsal.dql.model.*
import com.farcsal.dql.resolver.generated.DqlBaseListener
import com.farcsal.dql.resolver.generated.DqlParser.*
import com.farcsal.dql.resolver.variable.DqlVariableResolver
import org.apache.commons.text.StringEscapeUtils
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

internal class DqlResolverListener(private val variableResolver: DqlVariableResolver) : DqlBaseListener() {

    companion object {

        private val STRING_ARRAY_PATTERN = Pattern.compile("\\[(\"[^\"]*\")(,(\"[^\"]*\"))*]")
        private val STRING_LITERAL_PATTERN = Pattern.compile("\"[^\"]*\"")

        private val logger = LoggerFactory.getLogger(DqlResolverListener::class.java)

    }

    private var result: DqlExpression? = null

    private var field: String? = null
    private var value: DqlCriteriaValue? = null
    private var negatedCriteria = false
    private var builder: DqlExpressionBuilder? = null
    private val builderStack: Stack<DqlExpressionBuilder> = DequeStack()

    fun getResult(): DqlExpression {
        return result ?: throw IllegalStateException("Result is not ready")
    }

    override fun enterSentence(ctx: SentenceContext?) {
        logger.debug("enterSentence")
        builder = DqlExpression.builder()
        builderStack.push(requireBuilder())
    }

    override fun exitSentence(ctx: SentenceContext?) {
        result = builderStack.pop().build()
        logger.debug("exitSentence: {}", result)
    }

    override fun enterExpression(ctx: ExpressionContext) {
        logger.debug("enterExpression")
        builderStack.push(requireBuilder())
        builder = DqlExpression.builder()
        requireBuilder().negated(ctx.NOT() != null)
    }

    override fun exitExpression(ctx: ExpressionContext?) {
        val part = requireBuilder().build()
        builder = builderStack.pop()
        requireBuilder().addPart(part)
        logger.debug("exitExpression: {}", part)
    }

    override fun enterCriteria(ctx: CriteriaContext) {
        logger.debug("enterCriteria")
        negatedCriteria = ctx.NOT() != null
    }

    override fun exitBinaryCriteria(ctx: BinaryCriteriaContext) {
        val method = parseBinaryMethod(ctx.BINARY_OPERATION().symbol.text)
        val criteria = DqlCriteria(
            negated = negatedCriteria,
            field = requireField(),
            method = method,
            value = requireValue()
        )
        logger.debug("exitBinaryCriteria: {}", criteria)
        requireBuilder().addPart(criteria)
    }

    override fun exitUnaryCriteria(ctx: UnaryCriteriaContext) {
        val method = parseUnaryMethod(ctx.UNARY_OPERATION().symbol.text)
        val criteria = DqlCriteria(
            negated = negatedCriteria,
            field = requireField(),
            method = method,
            value = DqlCriteriaValue.ofUnary()
        )
        logger.debug("exitUnaryCriteria: {}", criteria)
        requireBuilder().addPart(criteria)
    }

    override fun enterField(ctx: FieldContext) {
        field = ctx.text
        logger.debug("enterField: {}", field)
    }

    override fun enterValue(ctx: ValueContext) {
        value = if (ctx.LITERAL() != null) {
            parseLiteral(ctx.LITERAL().symbol.text)
        } else {
            parseArray(ctx.ARRAY().symbol.text)
        }
        logger.debug("enterValue: {}", value)
    }

    override fun enterLogicalOperator(ctx: LogicalOperatorContext) {
        val op = parseOp(ctx.LOGICAL().symbol.text)
        logger.debug("enterLogicalOperator: {}", op)
        requireBuilder().addPart(op)
    }

    private fun parseNumber(text: String): DqlNumber {
        return DqlNumber(text.toBigDecimal())
    }

    private fun parseBinaryMethod(text: String): String {
        return removeDelimiters(text, true)
    }

    private fun parseUnaryMethod(text: String): String {
        return removeDelimiters(text, false)
    }

    private fun parseString(text: String): String {
        return StringEscapeUtils.unescapeJava(removeDelimiters(text, true))
    }

    private fun removeDelimiters(original: String, bothSide: Boolean): String {
        var text = original
        text = text.substring(1)
        if (bothSide) {
            text = text.substring(0, text.length - 1)
        }
        return text
    }

    private fun parseOp(text: String): DqlOp {
        return when (text) {
            "&" -> DqlOp.AND
            "|" -> DqlOp.OR
            else -> throw UnsupportedOperationException("Unsupported logical signal: $text")
        }
    }

    private fun parseLiteral(value: String): DqlCriteriaValue {
        if (value.startsWith("{") && value.endsWith("}")) {
            val name = value.substring(1, value.length - 1)
            return variableResolver.resolveVariable(name).apply {
                if (DqlCriteriaValueType.UNARY == valueType) {
                    throw UnsupportedOperationException("Unsupported value type for variable: $valueType")
                }
            }
        }
        return if (value.startsWith("\"") && value.endsWith("\"")) {
            DqlCriteriaValue.ofString(parseString(value))
        } else {
            DqlCriteriaValue.ofNumber(parseNumber(value))
        }
    }

    private fun parseArray(value: String): DqlCriteriaValue {
        return if (value.contains("\"")) {
            require(STRING_ARRAY_PATTERN.matcher(value).matches()) {
                "Not a string array. Maybe the array has non string literals too."
            }
            val b: MutableList<String> = ArrayList()
            val m = STRING_LITERAL_PATTERN.matcher(value)
            while (m.find()) {
                b.add(parseString(m.group()))
            }
            DqlCriteriaValue.ofStringList(b)
        } else {
            val numberValue = removeDelimiters(value, true)
            DqlCriteriaValue.ofNumberList(numberValue.split(",").toTypedArray()
                .map { s: String -> parseNumber(s) }
            )
        }
    }

    private fun requireBuilder(): DqlExpressionBuilder {
        return builder ?: throw NullPointerException()
    }

    private fun requireField(): String {
        return field ?: throw NullPointerException()
    }

    private fun requireValue(): DqlCriteriaValue {
        return value ?: throw NullPointerException()
    }

}
