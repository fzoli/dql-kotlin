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
package com.farcsal.dql.string.builder.filter

import com.farcsal.datatype.stack.DequeStack
import com.farcsal.datatype.stack.Stack
import com.farcsal.dql.model.DqlNumber
import com.farcsal.dql.model.DqlOp
import org.apache.commons.lang3.Validate
import org.apache.commons.text.StringEscapeUtils
import java.util.stream.Collectors

class DqlStringBuilder private constructor() {

    companion object {

        private const val OP_AND = '&'
        private const val OP_OR = '|'
        private const val OP_NOT = '!'
        private const val DELIMITER_METHOD = ':'
        private const val DELIMITER_STRING = '"'
        private const val DELIMITER_VARIABLE_BEGIN = '{'
        private const val DELIMITER_VARIABLE_END = '}'
        private const val DELIMITER_LIST_BEGIN = '['
        private const val DELIMITER_LIST_END = ']'
        private const val DELIMITER_EXPR_BEGIN = '('
        private const val DELIMITER_EXPR_END = ')'
        private const val DELIMITER_LIST_VALUE = ","

        fun builder(): DqlStringBuilder {
            return DqlStringBuilder()
        }

    }

    private val partStack: Stack<DqlStringBuilderPart> = DequeStack()

    private var part: DqlStringBuilderPart = DqlStringBuilderPart()

    fun build(): String? {
        if (part.size == 0) {
            return null
        }
        validateEnd()
        return part.builder.toString()
    }

    fun appendOp(op: DqlOp): DqlStringBuilder {
        checkOpPosition()
        when (op) {
            DqlOp.AND -> part.builder.append(OP_AND)
            DqlOp.OR -> part.builder.append(OP_OR)
            else -> throw UnsupportedOperationException("Unhandled OP: $op")
        }
        part.size++
        return this
    }

    fun beginExpression(): DqlStringBuilder {
        return beginExpression(false)
    }

    fun beginNegatedExpression(): DqlStringBuilder {
        return beginExpression(true)
    }

    fun beginExpression(negated: Boolean): DqlStringBuilder {
        checkExpressionPosition()
        appendNot(negated)
        part.builder.append(DELIMITER_EXPR_BEGIN)
        partStack.push(part)
        part = DqlStringBuilderPart()
        return this
    }

    fun endExpression(): DqlStringBuilder {
        check(part.size != 0) { "Empty expression" }
        validateEnd()
        val text = part.builder.toString()
        part = partStack.pop()
        part.builder.append(text)
        part.builder.append(DELIMITER_EXPR_END)
        part.size++
        return this
    }

    fun appendUnaryCriteria(
        field: String,
        method: String
    ): DqlStringBuilder {
        return appendUnaryCriteria(false, field, method)
    }

    fun appendNegatedUnaryCriteria(
        field: String,
        method: String
    ): DqlStringBuilder {
        return appendUnaryCriteria(true, field, method)
    }

    fun appendUnaryCriteria(
        negated: Boolean,
        field: String,
        method: String
    ): DqlStringBuilder {
        validateField(field)
        validateMethod(method)
        checkCriteriaPosition()
        appendNot(negated)
        part.builder.append(field)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(method)
        part.size++
        return this
    }

    fun appendStringCriteria(
        field: String,
        method: String,
        value: String
    ): DqlStringBuilder {
        return appendStringCriteria(false, field, method, value)
    }

    fun appendNegatedStringCriteria(
        field: String,
        method: String,
        value: String
    ): DqlStringBuilder {
        return appendStringCriteria(true, field, method, value)
    }

    fun appendStringCriteria(
        negated: Boolean,
        field: String,
        method: String,
        value: String
    ): DqlStringBuilder {
        validateField(field)
        validateMethod(method)
        checkCriteriaPosition()
        appendNot(negated)
        part.builder.append(field)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(method)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(DELIMITER_STRING)
        part.builder.append(StringEscapeUtils.escapeJava(value))
        part.builder.append(DELIMITER_STRING)
        part.size++
        return this
    }

    fun appendNumberCriteria(
        field: String,
        method: String,
        value: DqlNumber
    ): DqlStringBuilder {
        return appendNumberCriteria(false, field, method, value)
    }

    fun appendNegatedNumberCriteria(
        field: String,
        method: String,
        value: DqlNumber
    ): DqlStringBuilder {
        return appendNumberCriteria(true, field, method, value)
    }

    fun appendNumberCriteria(
        negated: Boolean,
        field: String,
        method: String,
        value: DqlNumber
    ): DqlStringBuilder {
        validateField(field)
        validateMethod(method)
        checkCriteriaPosition()
        appendNot(negated)
        part.builder.append(field)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(method)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(numberToString(value))
        part.size++
        return this
    }

    fun appendVariableCriteria(
        field: String,
        method: String,
        value: String
    ): DqlStringBuilder {
        return appendVariableCriteria(false, field, method, value)
    }

    fun appendNegatedVariableCriteria(
        field: String,
        method: String,
        value: String
    ): DqlStringBuilder {
        return appendVariableCriteria(true, field, method, value)
    }

    fun appendVariableCriteria(
        negated: Boolean,
        field: String,
        method: String,
        value: String
    ): DqlStringBuilder {
        validateField(field)
        validateMethod(method)
        check(!(value.contains(DELIMITER_VARIABLE_BEGIN) || value.contains(DELIMITER_VARIABLE_END))) { "Invalid variable name: $value" }
        checkCriteriaPosition()
        appendNot(negated)
        part.builder.append(field)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(method)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(DELIMITER_VARIABLE_BEGIN)
        part.builder.append(value)
        part.builder.append(DELIMITER_VARIABLE_END)
        part.size++
        return this
    }

    fun appendNumberListCriteria(
        field: String,
        method: String,
        value: Collection<DqlNumber>
    ): DqlStringBuilder {
        return appendNumberListCriteria(false, field, method, value)
    }

    fun appendNegatedNumberListCriteria(
        field: String,
        method: String,
        value: Collection<DqlNumber>
    ): DqlStringBuilder {
        return appendNumberListCriteria(true, field, method, value)
    }

    fun appendNumberListCriteria(
        negated: Boolean,
        field: String,
        method: String,
        value: Collection<DqlNumber>
    ): DqlStringBuilder {
        validateField(field)
        validateMethod(method)
        checkCriteriaPosition()
        appendNot(negated)
        part.builder.append(field)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(method)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(DELIMITER_LIST_BEGIN)
        part.builder.append(joinNumber(value))
        part.builder.append(DELIMITER_LIST_END)
        part.size++
        return this
    }

    fun appendStringListCriteria(
        field: String,
        method: String,
        value: Collection<String>
    ): DqlStringBuilder {
        return appendStringListCriteria(false, field, method, value)
    }

    fun appendNegatedStringListCriteria(
        field: String,
        method: String,
        value: Collection<String>
    ): DqlStringBuilder {
        return appendStringListCriteria(true, field, method, value)
    }

    fun appendStringListCriteria(
        negated: Boolean,
        field: String,
        method: String,
        value: Collection<String>
    ): DqlStringBuilder {
        Validate.notNull(value, "value")
        validateField(field)
        validateMethod(method)
        checkCriteriaPosition()
        appendNot(negated)
        part.builder.append(field)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(method)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(DELIMITER_LIST_BEGIN)
        part.builder.append(joinString(value))
        part.builder.append(DELIMITER_LIST_END)
        part.size++
        return this
    }

    fun appendVariableListCriteria(
        field: String,
        method: String,
        value: Collection<String>
    ): DqlStringBuilder {
        return appendVariableListCriteria(false, field, method, value)
    }

    fun appendNegatedVariableListCriteria(
        field: String,
        method: String,
        value: Collection<String>
    ): DqlStringBuilder {
        return appendVariableListCriteria(true, field, method, value)
    }

    fun appendVariableListCriteria(
        negated: Boolean,
        field: String,
        method: String,
        value: Collection<String>
    ): DqlStringBuilder {
        validateField(field)
        validateMethod(method)
        value.forEach {
            check(!(it.contains(DELIMITER_VARIABLE_BEGIN) || it.contains(DELIMITER_VARIABLE_END))) { "Invalid variable name: $it" }
        }
        checkCriteriaPosition()
        appendNot(negated)
        part.builder.append(field)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(method)
        part.builder.append(DELIMITER_METHOD)
        part.builder.append(DELIMITER_LIST_BEGIN)
        part.builder.append(joinVariable(value))
        part.builder.append(DELIMITER_LIST_END)
        part.size++
        return this
    }

    private fun joinVariable(value: Collection<String>): String {
        return value.stream()
            .map { s: String ->
                DELIMITER_VARIABLE_BEGIN + s + DELIMITER_VARIABLE_END
            }
            .collect(Collectors.joining(DELIMITER_LIST_VALUE))
    }

    private fun joinString(value: Collection<String>): String {
        return value.stream()
            .map { s: String ->
                DELIMITER_STRING + StringEscapeUtils.escapeJava(s) + DELIMITER_STRING
            }
            .collect(Collectors.joining(DELIMITER_LIST_VALUE))
    }

    private fun joinNumber(values: Collection<DqlNumber>): String {
        return values.stream()
            .map { value ->
                numberToString(
                    value
                )
            }
            .collect(Collectors.joining(DELIMITER_LIST_VALUE))
    }

    private fun numberToString(value: DqlNumber): String {
        return value.toBigDecimal().toPlainString()
    }

    private fun appendNot(negated: Boolean) {
        if (negated) {
            part.builder.append(OP_NOT)
        }
    }

    private fun validateMethod(method: String) {
        require(method.isNotEmpty()) { "Method can not be empty" }
    }

    private fun validateField(field: String) {
        require(field.isNotEmpty()) { "Field can not be empty" }
    }

    private fun checkCriteriaPosition() {
        require(part.size % 2 == 0) { "Criteria is not expected" }
    }

    private fun checkExpressionPosition() {
        require(part.size % 2 == 0) { "Expression is not expected" }
    }

    private fun checkOpPosition() {
        require(part.size % 2 != 0) { "OP is not expected" }
    }

    private fun validateEnd() {
        check(part.size % 2 != 0) { "OP at the end is not allowed" }
    }

}
