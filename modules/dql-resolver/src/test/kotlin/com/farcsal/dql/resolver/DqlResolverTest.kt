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

import com.farcsal.dql.model.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DqlResolverTest {

    @Test
    fun testStringCriteria() {
        val result: DqlExpression = DqlResolver().resolve("a:eq:\"b\"")
        Assertions.assertEquals(false, result.negated)
        Assertions.assertEquals(1, result.parts.size)
        val first: DqlExpressionPart = result.parts[0]
        Assertions.assertEquals(DqlExpressionPartType.CRITERIA, first.type)
        Assertions.assertEquals(false, first.requireCriteria().negated)
        Assertions.assertEquals("a", first.requireCriteria().field)
        Assertions.assertEquals("eq", first.requireCriteria().method)
        Assertions.assertEquals(DqlCriteriaValueType.STRING, first.requireCriteria().value.valueType)
        Assertions.assertEquals("b", first.requireCriteria().value.requireStringValue())
    }

    @Test
    fun testNumberCriteria() {
        val result: DqlExpression = DqlResolver().resolve("a:eq:1")
        Assertions.assertEquals(false, result.negated)
        Assertions.assertEquals(1, result.parts.size)
        val first: DqlExpressionPart = result.parts[0]
        Assertions.assertEquals(DqlExpressionPartType.CRITERIA, first.type)
        Assertions.assertEquals(false, first.requireCriteria().negated)
        Assertions.assertEquals("a", first.requireCriteria().field)
        Assertions.assertEquals("eq", first.requireCriteria().method)
        Assertions.assertEquals(DqlCriteriaValueType.NUMBER, first.requireCriteria().value.valueType)
        Assertions.assertEquals(DqlNumber(1), first.requireCriteria().value.requireNumberValue())
    }

    @Test
    fun testNumberListCriteria() {
        val result: DqlExpression = DqlResolver().resolve("a:in:[1,2]")
        Assertions.assertEquals(false, result.negated)
        Assertions.assertEquals(1, result.parts.size)
        val first: DqlExpressionPart = result.parts[0]
        Assertions.assertEquals(DqlExpressionPartType.CRITERIA, first.type)
        Assertions.assertEquals(false, first.requireCriteria().negated)
        Assertions.assertEquals("a", first.requireCriteria().field)
        Assertions.assertEquals("in", first.requireCriteria().method)
        Assertions.assertEquals(DqlCriteriaValueType.NUMBER_LIST, first.requireCriteria().value.valueType)
        val list = first.requireCriteria().value.requireNumberListValue()
        Assertions.assertEquals(2, list.size)
        Assertions.assertEquals(DqlNumber(1), list[0])
        Assertions.assertEquals(DqlNumber(2), list[1])
    }

    @Test
    fun testStringListCriteria() {
        val result: DqlExpression = DqlResolver().resolve("a:in:[\"one\",\"two\"]")
        Assertions.assertEquals(false, result.negated)
        Assertions.assertEquals(1, result.parts.size)
        val first: DqlExpressionPart = result.parts[0]
        Assertions.assertEquals(DqlExpressionPartType.CRITERIA, first.type)
        Assertions.assertEquals(false, first.requireCriteria().negated)
        Assertions.assertEquals("a", first.requireCriteria().field)
        Assertions.assertEquals("in", first.requireCriteria().method)
        Assertions.assertEquals(DqlCriteriaValueType.STRING_LIST, first.requireCriteria().value.valueType)
        val list = first.requireCriteria().value.requireStringListValue()
        Assertions.assertEquals(2, list.size.toLong())
        Assertions.assertEquals("one", list[0])
        Assertions.assertEquals("two", list[1])
    }

    @Test
    fun testMixedListCriteria() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            DqlResolver().resolve("a:in:[1,\"two\"]")
        }
    }

    @Test
    fun testEmpty() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            DqlResolver().resolve("")
        }
    }

    @Test
    fun testNull() {
        Assertions.assertThrows(NullPointerException::class.java) {
            DqlResolver().resolve(null)
        }
    }
    
    @Test
    fun testExpressionCriteria() {
        //    1.   2.  4.    3.
        // (a | b) & c & !(d | e)
        val result: DqlExpression = DqlResolver().resolve("(a:ma:1|b:mb:\"one\")&!c:mc:2&!(d:md|e:me)")
        Assertions.assertEquals(false, result.negated)
        Assertions.assertEquals(5, result.parts.size)
        val p0: DqlExpressionPart = result.parts[0]
        Assertions.assertEquals(DqlExpressionPartType.EXPRESSION, p0.type)
        Assertions.assertEquals(false, p0.requireExpression().negated)
        Assertions.assertEquals(3, p0.requireExpression().parts.size)
        val p0p0: DqlExpressionPart = p0.requireExpression().parts[0]
        Assertions.assertEquals(DqlExpressionPartType.CRITERIA, p0p0.type)
        Assertions.assertEquals(false, p0p0.requireCriteria().negated)
        Assertions.assertEquals("a", p0p0.requireCriteria().field)
        Assertions.assertEquals("ma", p0p0.requireCriteria().method)
        Assertions.assertEquals(DqlCriteriaValueType.NUMBER, p0p0.requireCriteria().value.valueType)
        Assertions.assertEquals(DqlNumber(1), p0p0.requireCriteria().value.requireNumberValue())
        val p0p1: DqlExpressionPart = p0.requireExpression().parts[1]
        Assertions.assertEquals(DqlExpressionPartType.OP, p0p1.type)
        Assertions.assertEquals(DqlOp.OR, p0p1.requireOp())
        val p0p2: DqlExpressionPart = p0.requireExpression().parts[2]
        Assertions.assertEquals(DqlExpressionPartType.CRITERIA, p0p2.type)
        Assertions.assertEquals(false, p0p2.requireCriteria().negated)
        Assertions.assertEquals("b", p0p2.requireCriteria().field)
        Assertions.assertEquals("mb", p0p2.requireCriteria().method)
        Assertions.assertEquals(DqlCriteriaValueType.STRING, p0p2.requireCriteria().value.valueType)
        Assertions.assertEquals("one", p0p2.requireCriteria().value.stringValue)
        val p1: DqlExpressionPart = result.parts[1]
        Assertions.assertEquals(DqlExpressionPartType.OP, p1.type)
        Assertions.assertEquals(DqlOp.AND, p1.requireOp())
        val p2: DqlExpressionPart = result.parts[2]
        Assertions.assertEquals(DqlExpressionPartType.CRITERIA, p2.type)
        Assertions.assertEquals(true, p2.requireCriteria().negated)
        Assertions.assertEquals("c", p2.requireCriteria().field)
        Assertions.assertEquals("mc", p2.requireCriteria().method)
        Assertions.assertEquals(DqlCriteriaValueType.NUMBER, p2.requireCriteria().value.valueType)
        Assertions.assertEquals(DqlNumber(2), p2.requireCriteria().value.requireNumberValue())
        val p3: DqlExpressionPart = result.parts[3]
        Assertions.assertEquals(DqlExpressionPartType.OP, p3.type)
        Assertions.assertEquals(DqlOp.AND, p3.requireOp())
        val p4: DqlExpressionPart = result.parts[4]
        Assertions.assertEquals(DqlExpressionPartType.EXPRESSION, p4.type)
        Assertions.assertEquals(true, p4.requireExpression().negated)
        Assertions.assertEquals(3, p4.requireExpression().parts.size)
        val p4p0: DqlExpressionPart = p4.requireExpression().parts[0]
        Assertions.assertEquals(DqlExpressionPartType.CRITERIA, p4p0.type)
        Assertions.assertEquals(false, p4p0.requireCriteria().negated)
        Assertions.assertEquals("d", p4p0.requireCriteria().field)
        Assertions.assertEquals("md", p4p0.requireCriteria().method)
        Assertions.assertEquals(DqlCriteriaValueType.UNARY, p4p0.requireCriteria().value.valueType)
        val p4p1: DqlExpressionPart = p4.requireExpression().parts[1]
        Assertions.assertEquals(DqlExpressionPartType.OP, p4p1.type)
        Assertions.assertEquals(DqlOp.OR, p4p1.requireOp())
        val p4p2: DqlExpressionPart = p4.requireExpression().parts[2]
        Assertions.assertEquals(DqlExpressionPartType.CRITERIA, p4p2.type)
        Assertions.assertEquals(false, p4p2.requireCriteria().negated)
        Assertions.assertEquals("e", p4p2.requireCriteria().field)
        Assertions.assertEquals("me", p4p2.requireCriteria().method)
        Assertions.assertEquals(DqlCriteriaValueType.UNARY, p4p2.requireCriteria().value.valueType)
    }

    @Test
    fun testParserError() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            DqlResolver().resolve("a")
        }
    }

    @Test
    fun testLexerError() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            DqlResolver().resolve("a:eq:\"5")
        }
    }

}
