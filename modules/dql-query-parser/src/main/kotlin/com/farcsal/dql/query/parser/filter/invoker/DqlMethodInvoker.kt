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
package com.farcsal.dql.query.parser.filter.invoker

import com.farcsal.dql.model.DqlNumber
import com.farcsal.dql.query.parser.filter.invoker.strategy.DqlMethodInvokerStrategySelector
import com.farcsal.dql.query.parser.filter.resolver.DqlCriteriaExpressionParser
import com.farcsal.dql.query.parser.filter.resolver.DqlFilterFieldExpressionResolver
import com.farcsal.query.api.Criteria
import com.farcsal.query.api.Field

internal class DqlMethodInvoker(
    private val resolver: DqlFilterFieldExpressionResolver,
    private val fieldSelector: DqlMethodInvokerStrategySelector
) : DqlCriteriaExpressionParser {

    private fun select(gexpr: Field): DqlCriteriaExpressionParser {
        return fieldSelector.select(gexpr)
    }

    override fun resolveUnaryExpression(
        field: String,
        method: String
    ): Criteria {
        val gexpr: Field = resolver.getExpression(field)
        return select(gexpr).resolveUnaryExpression(field, method)
    }

    override fun resolveNumberExpression(
        field: String,
        method: String,
        number: DqlNumber
    ): Criteria {
        val gexpr: Field = resolver.getExpression(field)
        return select(gexpr).resolveNumberExpression(field, method, number)
    }

    override fun resolveStringExpression(
        field: String,
        method: String,
        string: String
    ): Criteria {
        val gexpr: Field = resolver.getExpression(field)
        return select(gexpr).resolveStringExpression(field, method, string)
    }


    override fun resolveNumberListExpression(
        field: String,
        method: String,
        numberList: List<DqlNumber>
    ): Criteria {
        val gexpr: Field = resolver.getExpression(field)
        return select(gexpr).resolveNumberListExpression(field, method, numberList)
    }

    override fun resolveStringListExpression(
        field: String,
        method: String,
        stringList: List<String>
    ): Criteria {
        val gexpr: Field = resolver.getExpression(field)
        return select(gexpr).resolveStringListExpression(field, method, stringList)
    }

}
