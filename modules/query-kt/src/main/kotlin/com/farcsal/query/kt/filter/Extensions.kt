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
package com.farcsal.query.kt.filter

import com.farcsal.query.api.Criteria
import com.farcsal.query.api.Order
import com.farcsal.query.api.filter.FilterFunction
import com.farcsal.query.api.order.OrderFunction

fun Criteria.toBoolean(): Boolean {
    return KCriteria.unwrap(this)
}

@Suppress("UNCHECKED_CAST")
private fun <T : Any> Order.toComparator(): Comparator<in T> {
    return (this as KOrder<T>).comparator
}

@Suppress("UNCHECKED_CAST")
private fun <T: Any, O> toComparator(order: OrderFunction<O>?, orderObject: O): Comparator<in T>? {
    if (order == null) {
        return null
    }
    val comparators: List<Comparator<in T>> = order(orderObject).map { it.toComparator() }
    var comparator: Comparator<in T>? = null
    val iterator = comparators.iterator()
    while (iterator.hasNext()) {
        val current = iterator.next()
        comparator = if (comparator == null) {
            current
        } else {
            comparator.thenComparing(current as Comparator<Any?>?)
        }
    }
    return comparator
}

fun <T, F> List<T>.invokeFilter(filter: FilterFunction<F>?, mapper: (T) -> F): List<T> {
    if (filter == null) {
        return this
    }
    return filter {
        filter(mapper(it))?.toBoolean() ?: true
    }
}

fun <T: Any, O> List<T>.invokeOrder(order: OrderFunction<O>?, orderObject: O): List<T> {
    val comparator = toComparator<T, O>(order, orderObject) ?: return this
    return sortedWith(comparator)
}
