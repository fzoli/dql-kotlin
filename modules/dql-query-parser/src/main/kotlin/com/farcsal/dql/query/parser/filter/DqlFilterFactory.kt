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
package com.farcsal.dql.query.parser.filter

import com.farcsal.dql.query.parser.filter.invoker.DqlMethodInvoker
import com.farcsal.dql.query.parser.filter.invoker.strategy.DqlMethodInvokerStrategyFactory
import com.farcsal.dql.query.parser.filter.invoker.strategy.DqlMethodInvokerStrategySelector
import com.farcsal.dql.query.parser.filter.invoker.strategyFactories
import com.farcsal.dql.query.parser.filter.invoker.withAnyStrategy
import com.farcsal.dql.query.parser.filter.resolver.DqlCriteriaExpressionResolver
import com.farcsal.dql.query.parser.filter.resolver.DqlFilterFieldExpressionResolver
import com.farcsal.dql.resolver.DqlResolver
import com.farcsal.dql.resolver.variable.DefaultDqlVariableResolver
import com.farcsal.dql.resolver.variable.DqlVariableResolver
import com.farcsal.query.api.Criteria

class DqlFilterFactory(
    variableResolver: DqlVariableResolver = DefaultDqlVariableResolver,
    methodInvokerStrategyFactories: List<DqlMethodInvokerStrategyFactory> = strategyFactories.withAnyStrategy(),
) {

    private val dqlResolver: DqlResolver = DqlResolver(variableResolver = variableResolver)
    private val fieldSelector = DqlMethodInvokerStrategySelector(methodInvokerStrategyFactories)

    fun create(query: String?, expressionResolver: DqlFilterFieldExpressionResolver): Criteria? {
        return try {
            val resolver = DqlCriteriaExpressionResolver(
                DqlMethodInvoker(expressionResolver, fieldSelector)
            )
            if (query.isNullOrEmpty()) {
                null
            } else {
                resolver.resolve(dqlResolver.resolve(query))
            }
        } catch (ex: Exception) {
            throw IllegalArgumentException(ex) // bad request
        }
    }

}
