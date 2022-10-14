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

import com.farcsal.query.api.Order
import com.farcsal.query.api.order.OrderFunction
import com.farcsal.query.api.order.evaluate
import com.querydsl.core.types.dsl.Expressions
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class OrderEqualityTest {

    private val firstName = QStringOrderField(Expressions.stringPath("first_name"))
    private val lastName = QStringOrderField(Expressions.stringPath("last_name"))
    private val age = QOrderField(Expressions.numberPath(Long::class.javaObjectType, "age"))
    private val size = QOrderField(Expressions.numberPath(Long::class.javaObjectType, "size"))

    @Test
    fun sameGeneralField() {
        Assertions.assertEquals(age.asc(), age.desc())
        Assertions.assertTrue(listOf(age.asc()).contains(age.desc()))
    }

    @Test
    fun sameStringField() {
        Assertions.assertEquals(firstName.withLocale(Locale.ENGLISH).asc(), firstName.withLocale(Locale.FRENCH).desc())
        Assertions.assertTrue(listOf(firstName.withLocale(Locale.ENGLISH).asc()).contains(firstName.withLocale(Locale.FRENCH).desc()))
    }

    @Test
    fun differentStringField() {
        Assertions.assertNotEquals(firstName.asc(), lastName.asc())
        Assertions.assertFalse(listOf(firstName.asc()).contains(lastName.asc()))
    }

    @Test
    fun differentGeneralField() {
        Assertions.assertNotEquals(age.asc(), size.asc())
        Assertions.assertFalse(listOf(age.asc()).contains(size.asc()))
    }

    @Test
    fun orderFunctionEvaluate() {
        var fnFirstNameAsc: Order? = null
        val fn: OrderFunction<OrderEqualityTest> = {
            val firstNameAsc = firstName.asc()
            fnFirstNameAsc = firstNameAsc
            listOf(firstNameAsc)
        }
        val orders = fn.evaluate(this) {
            listOf(firstName.desc(), age.asc())
        }
        Assertions.assertEquals(2, orders.size)
        Assertions.assertTrue(fnFirstNameAsc === orders[0])
        Assertions.assertEquals(age.asc(), orders[1])
    }

}
