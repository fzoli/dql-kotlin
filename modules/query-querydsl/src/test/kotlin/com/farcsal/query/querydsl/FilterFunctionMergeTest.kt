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
package com.farcsal.query.querydsl

import com.farcsal.query.api.filter.FilterFunction
import com.farcsal.query.api.filter.and
import com.farcsal.query.api.filter.evaluate
import com.querydsl.core.types.dsl.Expressions
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FilterFunctionMergeTest {

    private val firstName = QStringField(Expressions.stringPath("first_name"))
    private val lastName = QStringField(Expressions.stringPath("last_name"))

    @Test
    fun and() {
        val left: FilterFunction<FilterFunctionMergeTest> = { this.firstName.isNotEmpty() }
        val right: FilterFunction<FilterFunctionMergeTest> = { this.lastName.isNotEmpty() }
        val merged = left.and(right)
        Assertions.assertEquals("!empty(first_name) && !empty(last_name)", merged.evaluate(this).toString())
    }

    @Test
    fun andNullLeft() {
        val left: FilterFunction<FilterFunctionMergeTest> = { null }
        val right: FilterFunction<FilterFunctionMergeTest> = { this.lastName.isNotEmpty() }
        val merged = left.and(right)
        Assertions.assertEquals("!empty(last_name)", merged.evaluate(this).toString())
    }

    @Test
    fun andAbsentLeft() {
        val left: FilterFunction<FilterFunctionMergeTest>? = null
        val right: FilterFunction<FilterFunctionMergeTest> = { this.lastName.isNotEmpty() }
        val merged = left.and(right)
        Assertions.assertEquals("!empty(last_name)", merged.evaluate(this).toString())
    }

    @Test
    fun andNullRight() {
        val left: FilterFunction<FilterFunctionMergeTest> = { this.firstName.isNotEmpty() }
        val right: FilterFunction<FilterFunctionMergeTest> = { null }
        val merged = left.and(right)
        Assertions.assertEquals("!empty(first_name)", merged.evaluate(this).toString())
    }

    @Test
    fun andAbsentRight() {
        val left: FilterFunction<FilterFunctionMergeTest> = { this.firstName.isNotEmpty() }
        val right: FilterFunction<FilterFunctionMergeTest>? = null
        val merged = left.and(right)
        Assertions.assertEquals("!empty(first_name)", merged.evaluate(this).toString())
    }

    @Test
    fun andBothNull() {
        val left: FilterFunction<FilterFunctionMergeTest> = { null }
        val right: FilterFunction<FilterFunctionMergeTest> = { null }
        val merged = left.and(right)
        Assertions.assertNull(merged.evaluate(this))
    }

    @Test
    fun andBothAbsent() {
        val left: FilterFunction<FilterFunctionMergeTest>? =  null
        val right: FilterFunction<FilterFunctionMergeTest>? = null
        val merged = left.and(right)
        Assertions.assertNull(merged.evaluate(this))
    }

}
