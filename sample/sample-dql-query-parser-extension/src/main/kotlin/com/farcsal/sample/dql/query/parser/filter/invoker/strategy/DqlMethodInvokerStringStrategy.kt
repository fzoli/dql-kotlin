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
package com.farcsal.sample.dql.query.parser.filter.invoker.strategy

import com.farcsal.dql.model.DqlNumber
import com.farcsal.dql.query.parser.filter.invoker.strategy.DqlMethodInvokerStrategy
import com.farcsal.dql.query.parser.filter.invoker.strategy.DqlMethodInvokerStringStrategy
import com.farcsal.query.api.Criteria
import com.farcsal.sample.query.api.DqlExtendedMethods
import com.farcsal.sample.query.api.UnaccentStringField

internal class DqlMethodInvokerUnaccentStringStrategy(
    private val delegate: DqlMethodInvokerStringStrategy,
    private val expr: UnaccentStringField
) : DqlMethodInvokerStrategy {
    
    override fun resolveUnaryExpression(
        field: String,
        method: String
    ): Criteria {
        return delegate.resolveUnaryExpression(field, method)
    }

    override fun resolveNumberExpression(
        field: String,
        method: String,
        number: DqlNumber
    ): Criteria {
        return delegate.resolveNumberExpression(field, method, number)
    }

    override fun resolveStringExpression(
        field: String,
        method: String,
        string: String
    ): Criteria {
        return when (method) {
            DqlExtendedMethods.CONTAINS_IGNORE_ACCENT -> expr.containsIgnoreAccent(string)
            DqlExtendedMethods.CONTAINS_IGNORE_ACCENT_CASE -> expr.containsIgnoreAccentCase(string)
            else -> delegate.resolveStringExpression(field, method, string)
        }
    }

    override fun resolveNumberListExpression(
        field: String,
        method: String,
        numberList: List<DqlNumber>
    ): Criteria {
        return delegate.resolveNumberListExpression(field, method, numberList)
    }

    override fun resolveStringListExpression(
        field: String,
        method: String,
        stringList: List<String>
    ): Criteria {
        rejectDuplicates(field, stringList)
        return delegate.resolveStringListExpression(field, method, stringList)
    }

}
