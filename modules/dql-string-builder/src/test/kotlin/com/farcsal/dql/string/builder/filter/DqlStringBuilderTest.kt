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

import com.farcsal.dql.model.DqlNumber
import com.farcsal.dql.model.DqlOp
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DqlStringBuilderTest {

    @Test
    fun testBuilder1() {
        val result = DqlStringBuilder.builder()
            .appendNumberCriteria("a", "one", DqlNumber(1))
            .appendOp(DqlOp.AND)
            .beginNegatedExpression()
            .appendNumberCriteria("b", "two", DqlNumber(2))
            .appendOp(DqlOp.OR)
            .appendNegatedUnaryCriteria("c", "three")
            .appendOp(DqlOp.OR)
            .beginExpression()
            .appendStringCriteria("h", "eight", "8 as string")
            .appendOp(DqlOp.AND)
            .appendStringCriteria("i", "nine", "9 as string")
            .endExpression()
            .endExpression()
            .appendOp(DqlOp.AND)
            .appendNumberListCriteria("d", "four", listOf(DqlNumber(4.1), DqlNumber(4.2)))
            .appendOp(DqlOp.AND)
            .appendNumberListCriteria("e", "five", listOf(DqlNumber(5.0)))
            .appendOp(DqlOp.AND)
            .appendStringListCriteria("f", "six", listOf("a", "b"))
            .appendOp(DqlOp.AND)
            .appendStringCriteria("g", "seven", "7 as string")
            .build()
        Assertions.assertEquals(
            "a:one:1&!(b:two:2|!c:three|(h:eight:\"8 as string\"&i:nine:\"9 as string\"))" +
            "&d:four:[4.1,4.2]&e:five:[5.0]&f:six:[\"a\",\"b\"]&g:seven:\"7 as string\"",
            result
        )
    }

    @Test
    fun testBuilder2() {
        val result = DqlStringBuilder.builder()
            .beginNegatedExpression()
            .appendUnaryCriteria("a", "isNotNull")
            .appendOp(DqlOp.OR)
            .appendUnaryCriteria("a", "isNotEmpty")
            .appendOp(DqlOp.OR)
            .appendUnaryCriteria("b", "isTrue")
            .endExpression()
            .appendOp(DqlOp.AND)
            .appendUnaryCriteria("b", "isTrue")
            .build()
        Assertions.assertEquals("!(a:isNotNull|a:isNotEmpty|b:isTrue)&b:isTrue", result)
    }

}
