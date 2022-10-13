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

import com.farcsal.dql.query.parser.order.field.decorator.OrderFieldDecorator
import com.farcsal.query.api.*
import com.farcsal.query.api.order.OrderFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

fun <T : Any> createFallback(decorator: OrderFieldDecorator): OrderFunction<T> {
    return {
        collectOrderCreators().map {
            it.mapper(decorator.decorate(it.field))
        }
    }
}

private fun Any.collectOrderCreators(): List<OrderCreator> {
    // Try Java fields first because iterating Java fields are 10-20 times faster
    // than iterating properties of Kotlin classes.
    val wrappers = findJavaFields().ifEmpty { findKotlinProperties() }
    val priorities = wrappers.map { it.config.priority }.distinct()
    if (priorities.size != wrappers.size) {
        throw IllegalStateException("Priority configuration of " + DefaultOrder::class.simpleName + " must be unique in the whole object")
    }
    return wrappers.sortedBy { it.config.priority }.map { wrapper ->
        val creator: (OrderField) -> Order = { orderField ->
            val order = if (wrapper.config.direction == DefaultOrder.Direction.ASC) {
                orderField.asc()
            } else {
                orderField.desc()
            }
            if (wrapper.config.nullHandling == DefaultOrder.NullHandling.NULLS_FIRST) {
                order.nullsFirst()
            } else {
                order.nullsLast()
            }
        }
        OrderCreator(wrapper.field, creator)
    }
}

private fun Any.findJavaFields(): List<Wrapper> = this::class.java.declaredFields.flatMap {
    it.trySetAccessible()
    val field = it.get(this)
    requireNotNull(field)
    val wrappers = mutableListOf<Wrapper>()
    if (field is OrderField) {
        val config = it.getAnnotation(DefaultOrder::class.java)
            ?: return@flatMap wrappers
        wrappers.add(Wrapper(config, field))
    } else {
        it.getAnnotation(EmbeddedDefaultOrder::class.java)
            ?: return@flatMap wrappers
        wrappers.addAll(field.findJavaFields())
    }
    wrappers
}

private fun Any.findKotlinProperties(): List<Wrapper> = this::class.memberProperties.flatMap {
    val field = it.getter.call(this)
    requireNotNull(field)
    val wrappers = mutableListOf<Wrapper>()
    if (field is OrderField) {
        val config = it.findAnnotation<DefaultOrder>()
            ?: return@flatMap wrappers
        wrappers.add(Wrapper(config, field))
    } else {
        it.findAnnotation<EmbeddedDefaultOrder>()
            ?: return@flatMap wrappers
        wrappers.addAll(field.findKotlinProperties())
    }
    wrappers
}

private data class Wrapper(
    val config: DefaultOrder,
    val field: OrderField
)

private class OrderCreator(
    val field: OrderField,
    val mapper: (OrderField) -> Order
)
