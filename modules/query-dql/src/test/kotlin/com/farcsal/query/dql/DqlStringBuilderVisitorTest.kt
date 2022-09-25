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

import com.farcsal.dql.model.DqlMethods
import com.farcsal.dql.model.DqlOp
import com.farcsal.dql.string.builder.filter.DqlStringBuilder
import com.farcsal.dql.string.builder.filter.factory.DqlStringBuilderVisitor
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DqlStringBuilderVisitorTest {

    @Test
    fun testVisitor8() {
        val visitor = DqlStringBuilderVisitor.ofOp(
            DqlStringBuilderVisitor.ofOp(
                DqlStringBuilderVisitor.ofOp(
                    DqlStringBuilderVisitor.ofUnary("a", DqlMethods.IS_NOT_NULL),
                    DqlOp.OR,
                    DqlStringBuilderVisitor.ofUnary("a", DqlMethods.IS_NOT_EMPTY)
                ),
                DqlOp.OR,
                DqlStringBuilderVisitor.ofUnary("b", DqlMethods.IS_TRUE)
            ).not(),
            DqlOp.AND,
            DqlStringBuilderVisitor.ofUnary("b", DqlMethods.IS_FALSE)
        )
        val builder: DqlStringBuilder = DqlStringBuilder.builder()
        visitor.visit(builder)
        val result = builder.build()
        Assertions.assertEquals("!(a:isNotNull|a:isNotEmpty|b:isTrue)&b:isFalse", result)
    }

}
