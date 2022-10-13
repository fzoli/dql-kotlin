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
package com.farcsal.dql.query.parser.order.field.general

import com.farcsal.dql.query.parser.order.OrderFunctionFactory
import com.farcsal.dql.query.parser.order.field.DqlOrderFieldParser
import com.farcsal.dql.query.parser.order.field.decorator.OrderFieldDecorator
import com.farcsal.query.api.order.OrderFunction

class GeneralDqlOrderFieldParser<T : Any>(
    private val functionFactory: OrderFunctionFactory,
    private val fallbackDecorator: OrderFieldDecorator,
) : DqlOrderFieldParser<T> {

    override fun parseOrder(order: String?): OrderFunction<T> {
        return functionFactory.create(order, createFallback(fallbackDecorator)) {
            GeneralDqlOrderFieldExpressionResolver(this)
        }
    }

}
