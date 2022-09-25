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
package com.farcsal.dql.query.parser

import com.querydsl.core.types.dsl.Expressions
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Module tests are based on QueryDSL.
 * Here is the definition how QueryDSL works.
 */
class QueryDslTest {

    @Test
    fun queryDslComplex1() {
        val a = Expressions.stringPath("a").isNull()
        val b = Expressions.stringPath("b").isNull()
        val c = Expressions.stringPath("c").isNull()
        val d = Expressions.stringPath("d").isNull()
        val e = Expressions.stringPath("e").isNull()
        val result =
            a.and( b.and(c).or(d) ).or( e.not() ).not()
                .toString()
        Assertions.assertEquals("!(a is null && (b is null && c is null || d is null) || !(e is null))", result)
    }

    @Test
    fun queryDslComplex2() {
        val a = Expressions.stringPath("a").isNull()
        val b = Expressions.stringPath("b").isNull()
        val c = Expressions.stringPath("c").isNull()
        val d = Expressions.stringPath("d").isNull()
        val result = a.and(b.or(c)).and(d).toString()
        Assertions.assertEquals("a is null && (b is null || c is null) && d is null", result)
    }

    @Test
    fun queryDslComplex3() {
        val a = Expressions.stringPath("a").isNull()
        val b = Expressions.stringPath("b").isNull()
        val c = Expressions.stringPath("c").isNull()
        val d = Expressions.stringPath("d").isNull()
        val e = Expressions.stringPath("e").isNull()
        val result =
            a.or( b.and(c).or(d) ).or( e.not() ).not()
                .toString()
        Assertions.assertEquals("!(a is null || b is null && c is null || d is null || !(e is null))", result)
    }

    @Test
    fun queryDslComplex4() {
        val a = Expressions.stringPath("a").isNull()
        val b = Expressions.stringPath("b").isNull()
        val c = Expressions.stringPath("c").isNull()
        val d = Expressions.stringPath("d").isNull()
        val e = Expressions.stringPath("e").isNull()
        val result =
            a.or( b.or(c).or(d) ).or( e.not() ).not()
                .toString()
        Assertions.assertEquals("!(a is null || b is null || c is null || d is null || !(e is null))", result)
    }

    @Test
    fun queryDslComplex5() {
        val a = Expressions.stringPath("a").isNull()
        val b = Expressions.stringPath("b").isNull()
        val c = Expressions.stringPath("c").isNull()
        val result =
            a.and(b.or(c))
                .toString()
        Assertions.assertEquals("a is null && (b is null || c is null)", result)
    }

    @Test
    fun queryDslComplex6() {
        val a = Expressions.stringPath("a").isNull()
        val b = Expressions.stringPath("b").isNull()
        val c = Expressions.stringPath("c").isNull()
        val d = Expressions.stringPath("d").isNull()
        val result =
            a.and(b.and(c).or(d))
                .toString()
        Assertions.assertEquals("a is null && (b is null && c is null || d is null)", result)
    }

    @Test
    fun queryDslComplex7() {
        val a = Expressions.stringPath("a").isNull()
        val b = Expressions.stringPath("b").isNull()
        val c = Expressions.stringPath("c").isNull()
        val d = Expressions.stringPath("d").isNull()
        val result =
            a.or(b.and(c).or(d))
                .toString()
        Assertions.assertEquals("a is null || b is null && c is null || d is null", result)
    }

    @Test
    fun queryDslOpGroupingPresentation() {
        val age = Expressions.numberPath(Long::class.javaObjectType, "age")
        Assertions.assertEquals(
            "age > 1 && age > 2 || age > 3",
            age.gt(1).and(age.gt(2)).or(age.gt(3)).toString()
        )
        Assertions.assertEquals(
            "age > 1 && (age > 2 || age > 3)",
            age.gt(1).and(age.gt(2).or(age.gt(3))).toString()
        )
        Assertions.assertEquals(
            "!(age > 1 && (age > 2 || age > 3))",
            age.gt(1).and(age.gt(2).or(age.gt(3))).not().toString()
        )
        Assertions.assertEquals(
            "age > 1 && (age > 2 || age > 3) || age is not null",
            age.gt(1).and(age.gt(2).or(age.gt(3))).or(age.isNotNull()).toString()
        )
        Assertions.assertEquals(
            "(age > 1 || age > 2) && age > 3",
            age.gt(1).or(age.gt(2)).and(age.gt(3)).toString()
        )
        Assertions.assertEquals(
            "age > 3 && (age > 1 || age > 2)",
            age.gt(3).and(age.gt(1).or(age.gt(2))).toString()
        )
        Assertions.assertEquals(
            "(age > 1 || age > 2 || age > 3) && age > 4",
            age.gt(1).or(age.gt(2)).or(age.gt(3)).and(age.gt(4)).toString()
        )
        Assertions.assertEquals(
            "(age > 1 && age > 2 || age > 3) && age > 4",
            age.gt(1).and(age.gt(2)).or(age.gt(3)).and(age.gt(4)).toString()
        )
        Assertions.assertEquals(
            "((age > 1 || age > 2) && age > 3 || age > 4) && age > 5",
            age.gt(1).or(age.gt(2)).and(age.gt(3)).or(age.gt(4)).and(age.gt(5)).toString()
        )
        Assertions.assertEquals(
            "!(((age > 1 || age > 2) && age > 3 || age > 4) && age > 5)",
            age.gt(1).or(age.gt(2)).and(age.gt(3)).or(age.gt(4)).and(age.gt(5)).not().toString()
        )
        Assertions.assertEquals(
            "(!((age > 1 || age > 2) && age > 3) || age > 4) && age > 5",
            age.gt(1).or(age.gt(2)).and(age.gt(3)).not().or(age.gt(4)).and(age.gt(5)).toString()
        )
        Assertions.assertEquals(
            "(!(age > 1 || age > 2) && age > 3 || age > 4) && age > 5",
            age.gt(1).or(age.gt(2)).not().and(age.gt(3)).or(age.gt(4)).and(age.gt(5)).toString()
        )
        Assertions.assertEquals(
            "!(age > 1 || age > 2) && (age > 3 || age > 4) && age > 5",
            age.gt(1).or(age.gt(2)).not().and(age.gt(3).or(age.gt(4)).and(age.gt(5))).toString()
        )
    }

    @Test
    fun queryDslGroupingByPrecedence() {
        val a = Expressions.stringPath("a").isNull()
        val b = Expressions.stringPath("b").isNull()
        val c = Expressions.stringPath("c").isNull()
        val d = Expressions.stringPath("d").isNull()
        val result1 =
            a.or(b.and(c)) // a || (b && c)
                .toString()
        Assertions.assertEquals("a is null || b is null && c is null", result1)
        val result2 =
            a.or(b).and(c) // a || b && c
                .toString()
        Assertions.assertEquals("(a is null || b is null) && c is null", result2)
        val result3 =
            d.and(a.or(b).and(c)) // d && (a || b && c)
                .toString()
        Assertions.assertEquals("d is null && (a is null || b is null) && c is null", result3)
        val result4 =
            d.and(a.or(b.and(c))) // d && (a || (b && c))
                .toString()
        Assertions.assertEquals("d is null && (a is null || b is null && c is null)", result4)
    }

}
