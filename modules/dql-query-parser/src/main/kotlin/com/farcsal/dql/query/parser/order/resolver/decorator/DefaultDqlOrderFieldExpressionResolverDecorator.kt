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
package com.farcsal.dql.query.parser.order.resolver.decorator

import com.farcsal.dql.query.parser.order.field.decorator.OrderFieldDecorator
import com.farcsal.dql.query.parser.order.resolver.DqlOrderFieldExpressionResolver
import com.farcsal.query.api.OrderField

class DefaultDqlOrderFieldExpressionResolverDecorator(private val orderFieldDecorator: OrderFieldDecorator) : DqlOrderFieldExpressionResolverDecorator {
    override fun decorate(resolver: DqlOrderFieldExpressionResolver): DqlOrderFieldExpressionResolver {
        return DecoratedDqlOrderFieldExpressionResolver(resolver, orderFieldDecorator)
    }
}

private class DecoratedDqlOrderFieldExpressionResolver(
    private val delegate: DqlOrderFieldExpressionResolver,
    private val orderFieldDecorator: OrderFieldDecorator
): DqlOrderFieldExpressionResolver {
    override fun getExpression(field: String): OrderField {
        val order = delegate.getExpression(field)
        return orderFieldDecorator.decorate(order)
    }
}
