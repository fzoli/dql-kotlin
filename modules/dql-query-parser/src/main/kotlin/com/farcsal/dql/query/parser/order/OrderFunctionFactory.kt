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
package com.farcsal.dql.query.parser.order

import com.farcsal.dql.query.parser.order.field.decorator.DefaultOrderFieldDecorator
import com.farcsal.dql.query.parser.order.field.decorator.OrderFieldDecorator
import com.farcsal.dql.query.parser.order.resolver.DqlOrderFieldExpressionResolver
import com.farcsal.dql.query.parser.order.resolver.DqlOrderFieldExpressionResolverFn
import com.farcsal.dql.query.parser.order.resolver.decorator.DefaultDqlOrderFieldExpressionResolverDecorator
import com.farcsal.query.api.order.OrderFunction

class OrderFunctionFactory(
    private val factory: DqlOrderFactory,
    orderFieldDecorator: OrderFieldDecorator = DefaultOrderFieldDecorator,
) {

    private val decorator = DefaultDqlOrderFieldExpressionResolverDecorator(orderFieldDecorator)

    fun <T> create(
        order: String?,
        resolverFn: DqlOrderFieldExpressionResolverFn<T>
    ): OrderFunction<T> {
        return {
            val resolver = resolverFn(this).decorate()
            factory.create(order, resolver)
        }
    }

    private fun DqlOrderFieldExpressionResolver.decorate(): DqlOrderFieldExpressionResolver {
        return decorator.decorate(this)
    }

}
