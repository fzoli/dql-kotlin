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
package com.farcsal.dql.model

class DqlCriteriaValue internal constructor(

    val valueType: DqlCriteriaValueType,

    /**
     * null if not a string value or unary
     */
    val stringValue: String? = null,

    /**
     * null if not a number value or unary
     */
    val numberValue: DqlNumber? = null,

    /**
     * null if not a string list value or unary
     */
    val stringListValue: List<String>? = null,

    /**
     * null if not a number list value or unary
     */
    val numberListValue: List<DqlNumber>? = null

) {
    
    companion object {
        
        fun ofUnary(): DqlCriteriaValue {
            return DqlCriteriaValue(
                valueType = DqlCriteriaValueType.UNARY
            )
        }
        
        fun ofString(value: String): DqlCriteriaValue {
            return DqlCriteriaValue(
                valueType = DqlCriteriaValueType.STRING,
                stringValue = value
            )
        }

        fun ofStringList(value: List<String>): DqlCriteriaValue {
            return DqlCriteriaValue(
                valueType = DqlCriteriaValueType.STRING_LIST,
                stringListValue = value
            )
        }
        
        fun ofNumber(value: DqlNumber): DqlCriteriaValue {
            return DqlCriteriaValue(
                valueType = DqlCriteriaValueType.NUMBER,
                numberValue = value
            )
        }

        fun ofNumberList(value: List<DqlNumber>): DqlCriteriaValue {
            return DqlCriteriaValue(
                valueType = DqlCriteriaValueType.NUMBER_LIST,
                numberListValue = value
            )
        }

    }
    
    fun visit(visitor: DqlCriteriaValueVisitor) {
        when (valueType) {
            DqlCriteriaValueType.UNARY -> visitor.unary()
            DqlCriteriaValueType.NUMBER -> visitor.number(numberValue ?: throw NullPointerException("numberValue"))
            DqlCriteriaValueType.STRING -> visitor.string(stringValue ?: throw NullPointerException("stringValue"))
            DqlCriteriaValueType.NUMBER_LIST -> visitor.numberList(numberListValue ?: throw NullPointerException("numberListValue"))
            DqlCriteriaValueType.STRING_LIST -> visitor.stringList(stringListValue ?: throw NullPointerException("stringListValue"))
        }
    }

    override fun toString(): String {
        return when (valueType) {
            DqlCriteriaValueType.UNARY -> ""
            DqlCriteriaValueType.NUMBER -> numberValue.toString()
            DqlCriteriaValueType.STRING -> stringValue.toString()
            DqlCriteriaValueType.NUMBER_LIST -> numberListValue.toString()
            DqlCriteriaValueType.STRING_LIST -> stringListValue.toString()
        }
    }

    fun requireStringValue(): String {
        return stringValue ?: throw NullPointerException()
    }

    fun requireStringListValue(): List<String> {
        return stringListValue ?: throw NullPointerException()
    }

    fun requireNumberValue(): DqlNumber {
        return numberValue ?: throw NullPointerException()
    }

    fun requireNumberListValue(): List<DqlNumber> {
        return numberListValue ?: throw NullPointerException()
    }
    
}
