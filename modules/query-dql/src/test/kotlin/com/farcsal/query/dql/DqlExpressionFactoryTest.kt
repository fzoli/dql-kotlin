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
package com.farcsal.query.dql

import com.farcsal.dql.string.builder.filter.factory.DqlExpressionFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DqlExpressionFactoryTest {

    @Test
    fun testFactory1() {
        val fieldA = DqlBooleanField("a")
        val result = DqlExpressionFactory.create(fieldA.and(fieldA.not()))
        Assertions.assertEquals("a:isTrue&a:isFalse", result)
    }

    @Test
    fun testFactory2() {
        val fieldA = DqlBooleanField("a")
        val result = DqlExpressionFactory.create(fieldA.isTrue())
        Assertions.assertEquals("a:isTrue", result)
    }

    @Test
    fun testFactory3() {
        val fieldA = DqlBooleanField("a")
        val result = DqlExpressionFactory.create(fieldA.isFalse().not())
        Assertions.assertEquals("!a:isFalse", result)
    }

    @Test
    fun testFactory4() {
        val fieldA = DqlStringField("a")
        val result = DqlExpressionFactory.create(fieldA.eq("b").not())
        Assertions.assertEquals("!a:eq:\"b\"", result)
    }

    @Test
    fun testFactory5() {
        val fieldA = DqlStringField("a")
        val result = DqlExpressionFactory.create(
            fieldA.isNotNull().and(fieldA.isNotEmpty()))
        Assertions.assertEquals("a:isNotNull&a:isNotEmpty", result)
    }

    @Test
    fun testFactory6() {
        val fieldA = DqlStringField("a")
        val fieldB = DqlBooleanField("b")
        val result = DqlExpressionFactory.create(
            fieldB.isFalse().not().and(fieldA.isNotNull().or(fieldA.isNotEmpty()).or(fieldB)).not()
        )
        Assertions.assertEquals("!(!b:isFalse&(a:isNotNull|a:isNotEmpty|b:isTrue))", result)
    }

    @Test
    fun testFactory7() {
        val fieldA = DqlStringField("a")
        val fieldB = DqlBooleanField("b")
        val result = DqlExpressionFactory.create(
            fieldB.isFalse().not().and(fieldA.isNotNull().or(fieldA.isNotEmpty()).or(fieldB).not())
        )
        Assertions.assertEquals("!b:isFalse&!(a:isNotNull|a:isNotEmpty|b:isTrue)", result)
    }

    @Test
    fun testFactory8() {
        val fieldA = DqlStringField("a")
        val fieldB = DqlBooleanField("b")
        val result = DqlExpressionFactory.create(
            fieldA.isNotNull().or(fieldA.isNotEmpty()).or(fieldB).not().and(fieldB.not()))
        Assertions.assertEquals("!(a:isNotNull|a:isNotEmpty|b:isTrue)&b:isFalse", result)
    }

    @Test
    fun testFactory9() {
        val fieldA = DqlStringField("a")
        val fieldB = DqlBooleanField("b")
        val result = DqlExpressionFactory.create(
            fieldA.isNotNull().and(fieldA.isNotEmpty().and(fieldB.not())))
        Assertions.assertEquals("a:isNotNull&(a:isNotEmpty&b:isFalse)", result)
    }

    @Test
    fun testFactory10() {
        val fieldA = DqlStringField("a")
        val fieldB = DqlBooleanField("b")
        val result = DqlExpressionFactory.create(
            fieldA.isNotNull().and(fieldA.isNotEmpty().and(fieldB.not()).not()))
        Assertions.assertEquals("a:isNotNull&!(a:isNotEmpty&b:isFalse)", result)
    }

    @Test
    fun testFactory11() {
        val fieldA = DqlStringField("a")
        val fieldB = DqlBooleanField("b")
        val result = DqlExpressionFactory.create(
            fieldA.isNotNull().and(fieldA.isNotEmpty()).and(fieldB.not()))
        Assertions.assertEquals("a:isNotNull&a:isNotEmpty&b:isFalse", result)
    }

    @Test
    fun testFactory12() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val e = DqlBooleanField("e").isNull()
        // not(or(or(a, or(or(b, c), d)), not(e)))
        val result = DqlExpressionFactory.create(
            (a.or(b.and(c).or(d))).and(e.not()).not()
        )
        Assertions.assertEquals("!((a:isNull|(b:isNull&c:isNull|d:isNull))&!e:isNull)", result)
    }

    @Test
    fun testFactory13() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            (a.or(b)).and(c)
        )
        Assertions.assertEquals("(a:isNull|b:isNull)&c:isNull", result)
    }

    @Test
    fun testFactory14() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            c.and(a.or(b))
        )
        Assertions.assertEquals("c:isNull&(a:isNull|b:isNull)", result)
    }

    @Test
    fun testFactory15() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            a.or(b.and(c))
        )
        Assertions.assertEquals("a:isNull|b:isNull&c:isNull", result)
    }

    @Test
    fun testFactory16() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            a.or((b.or(c)).and(d))
        )
        Assertions.assertEquals("a:isNull|(b:isNull|c:isNull)&d:isNull", result)
    }

    @Test
    fun testFactory17() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            // ( 1 + ( 2 + 3 ) ) * 4
            a.or( b.or(c) ).and(d)
        )
        Assertions.assertEquals("(a:isNull|(b:isNull|c:isNull))&d:isNull", result)
    }

    @Test
    fun testFactory18() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            (a.or(b).or(c)).and(d)
        )
        Assertions.assertEquals("(a:isNull|b:isNull|c:isNull)&d:isNull", result)
    }

    @Test
    fun testFactory19() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            (a.and(b).or(c)).and(d)
        )
        Assertions.assertEquals("(a:isNull&b:isNull|c:isNull)&d:isNull", result)
    }

    @Test
    fun testFactory20() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            a.and(b.or(c))
        )
        Assertions.assertEquals("a:isNull&(b:isNull|c:isNull)", result)
    }

    @Test
    fun testFactory21() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            a.and(b.and(c).or(d))
        )
        Assertions.assertEquals("a:isNull&(b:isNull&c:isNull|d:isNull)", result)
    }

    @Test
    fun testFactory22() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            // 1 + ( (2 * 3) + 4 ) = 1 + ( 2 * 3 + 4 )
            a.or(b.and(c).or(d))
        )
        Assertions.assertEquals("a:isNull|(b:isNull&c:isNull|d:isNull)", result)
    }

    @Test
    fun testFactory23() {
        val resultSingle = DqlExpressionFactory.create(DqlStringField("a").equalsIgnoreCase(DqlVariable("name")))
        Assertions.assertEquals("a:equalsIgnoreCase:{name}", resultSingle)

        val resultList = DqlExpressionFactory.create(DqlStringField("a").memberOf(listOf(DqlVariable("a"))))
        Assertions.assertEquals("a:memberOf:[{a}]", resultList)
    }

    @Test
    fun testFactory24() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            // 1 * ((2 * 3) + 4) = 1 * (2 * 3 + 4)
            a.and( b.and(c).or(d) )
        )
        Assertions.assertEquals("a:isNull&(b:isNull&c:isNull|d:isNull)", result)
    }

    @Test
    fun testFactory25() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            // 1 * |((2 * 3) + 4)| = 1 * |2 * 3 + 4|
            a.and( b.and(c).or(d).not() )
        )
        Assertions.assertEquals("a:isNull&!(b:isNull&c:isNull|d:isNull)", result)
    }

    @Test
    fun testFactory26() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            // (1 * (2 * 3)) + 4 = 1 * (2 * 3) + 4
            a.and( b.and(c) ).or(d)
        )
        Assertions.assertEquals("a:isNull&(b:isNull&c:isNull)|d:isNull", result)
    }

    @Test
    fun testFactory27() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            // (1 * |(2 * 3)|) + 4 = 1 * |2 * 3| + 4
            a.and( b.and(c).not() ).or(d)
        )
        Assertions.assertEquals("a:isNull&!(b:isNull&c:isNull)|d:isNull", result)
    }

    @Test
    fun testFactory28() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            // 1 * ((2 * 3) * 4) = 1 * (2 * 3 * 4)
            a.and( b.and(c).and(d) )
        )
        Assertions.assertEquals("a:isNull&(b:isNull&c:isNull&d:isNull)", result)
    }

    @Test
    fun testFactory29() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val result = DqlExpressionFactory.create(
            // 1 * |((2 * 3) * 4)| = 1 * |2 * 3 * 4|
            a.and( b.and(c).and(d).not() )
        )
        Assertions.assertEquals("a:isNull&!(b:isNull&c:isNull&d:isNull)", result)
    }

    @Test
    fun testFactory30() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            // 1 * (2 * 3)
            a.and( b.and(c) )
        )
        Assertions.assertEquals("a:isNull&(b:isNull&c:isNull)", result)
    }

    @Test
    fun testFactory31() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            // 1 * |(2 * 3)|
            a.and( b.and(c).not() )
        )
        Assertions.assertEquals("a:isNull&!(b:isNull&c:isNull)", result)
    }

    @Test
    fun testFactory32() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            // (1 * 2) * 3 = 1 * 2 * 3
            a.and(b).and(c)
        )
        Assertions.assertEquals("a:isNull&b:isNull&c:isNull", result)
    }

    @Test
    fun testFactory33() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            // |(1 * 2)| * 3 = |1 * 2| * 3
            a.and(b).not().and(c)
        )
        Assertions.assertEquals("!(a:isNull&b:isNull)&c:isNull", result)
    }

    @Test
    fun testFactory34() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            // |(1 * 2) * 3| = |1 * 2 * 3|
            a.and(b).and(c).not()
        )
        Assertions.assertEquals("!(a:isNull&b:isNull&c:isNull)", result)
    }

    @Test
    fun testFactory35() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            // 1 + (2 * 3) = 1 + 2 * 3
            a.or(b.and(c))
        )
        Assertions.assertEquals("a:isNull|b:isNull&c:isNull", result)
    }

    @Test
    fun testFactory36() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            // 1 + |(2 * 3)| = 1 + |2 * 3|
            a.or(b.and(c).not())
        )
        Assertions.assertEquals("a:isNull|!(b:isNull&c:isNull)", result)
    }

    @Test
    fun testFactory37() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            // |1 + (2 * 3)| = |1 + 2 * 3|
            a.or(b.and(c)).not()
        )
        Assertions.assertEquals("!(a:isNull|b:isNull&c:isNull)", result)
    }

    @Test
    fun testFactory38() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            // (1 + 2) * 3
            a.or(b).and(c)
        )
        Assertions.assertEquals("(a:isNull|b:isNull)&c:isNull", result)
    }

    @Test
    fun testFactory39() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            // |(1 + 2)| * 3
            a.or(b).not().and(c)
        )
        Assertions.assertEquals("!(a:isNull|b:isNull)&c:isNull", result)
    }

    @Test
    fun testFactory40() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val result = DqlExpressionFactory.create(
            // |(1 + 2) * 3|
            a.or(b).and(c).not()
        )
        Assertions.assertEquals("!((a:isNull|b:isNull)&c:isNull)", result)
    }

    @Test
    fun testFactory41() {
        val a = DqlBooleanField("a").isNull()
        val b = DqlBooleanField("b").isNull()
        val c = DqlBooleanField("c").isNull()
        val d = DqlBooleanField("d").isNull()
        val e = DqlBooleanField("e").isNull()
        val result = DqlExpressionFactory.create(
            // 1 + ( ( (2 * 3) + 4 ) * 5 ) = 1 + (2 * 3 + 4) * 5
            a.or( ((b.and(c)).or(d)).and(e) )
        )
        Assertions.assertEquals("a:isNull|(b:isNull&c:isNull|d:isNull)&e:isNull", result)
    }

}
