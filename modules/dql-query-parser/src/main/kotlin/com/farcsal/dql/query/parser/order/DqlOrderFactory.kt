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

import com.farcsal.datatype.split
import com.farcsal.dql.query.parser.order.decoder.DqlOrderQueryParamTextDecoder
import com.farcsal.dql.query.parser.order.decoder.DqlOrderTextDecoder
import com.farcsal.dql.query.parser.order.resolver.DqlOrderFieldExpressionResolver
import com.farcsal.query.api.Order
import com.farcsal.query.api.OrderField

class DqlOrderFactory {

    private val orderTextDecoder: DqlOrderTextDecoder = DqlOrderQueryParamTextDecoder

    fun create(
        rawOrder: String?,
        expressionResolver: DqlOrderFieldExpressionResolver,
        fallback: List<Order>? = null
    ): List<Order> {
        if (fallback != null) {
            // Empty order list is invalid on API layer.
            // Relational databases would return random elements.
            require(fallback.isNotEmpty()) { "Empty fallback" }
        }
        val orderText = orderTextDecoder.decode(rawOrder)
        if (orderText.isNullOrEmpty()) {
            if (fallback != null) {
                return fallback
            }
            // Empty order list is invalid on API layer.
            // Relational databases would return random elements.
            throw IllegalArgumentException("Order is required")
        }
        return parseOrderFields(orderText) { fieldText, direction, nullStrategy ->
            toOrder(expressionResolver.getExpression(fieldText), direction, nullStrategy)
        }
    }

    private fun toOrder(orderField: OrderField, direction: DqlOrderDirection, nullStrategy: DqlOrderNullStrategy?): Order {
        val order = when (direction) {
            DqlOrderDirection.ASC -> orderField.asc().nullsLast()
            DqlOrderDirection.DESC -> orderField.desc().nullsFirst()
        }
        if (nullStrategy == null) {
            return order
        }
        return when (nullStrategy) {
            DqlOrderNullStrategy.FIRST -> order.nullsFirst()
            DqlOrderNullStrategy.LAST -> order.nullsLast()
        }
    }

    private fun <F> parseOrderFields(
        orderText: String,
        orderFieldProcessor: (field: String, direction: DqlOrderDirection, nullStrategy: DqlOrderNullStrategy?) -> F
    ): List<F> {
        return orderText.split(",") { orderItem ->
            val orderType = orderItem.substring(0, 1)
            var direction = DqlOrderDirection.ASC
            var orderField: String
            if (orderType.matches(Regex("[+\\-]"))) {
                direction = if (orderType == "-") DqlOrderDirection.DESC else DqlOrderDirection.ASC
                orderField = orderItem.substring(1)
            } else {
                orderField = orderItem
            }
            var nullStrategy: DqlOrderNullStrategy? = null
            if (orderField.startsWith("nl-")) {
                nullStrategy = DqlOrderNullStrategy.LAST
                orderField = orderField.substring(3)
            }
            else if (orderField.startsWith("nf-")) {
                nullStrategy = DqlOrderNullStrategy.FIRST
                orderField = orderField.substring(3)
            }
            orderFieldProcessor(orderField, direction, nullStrategy)
        }
    }

    private enum class DqlOrderDirection {
        ASC, DESC
    }

    private enum class DqlOrderNullStrategy {
        FIRST, LAST
    }

}
