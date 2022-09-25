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
package com.farcsal.query.querydsl.extension.query

import com.farcsal.query.api.Criteria
import com.farcsal.query.api.Order
import com.farcsal.query.querydsl.QBooleanField
import com.farcsal.query.querydsl.QOrder
import com.querydsl.core.support.QueryBase
import com.querydsl.core.types.OrderSpecifier

@Suppress("UNCHECKED_CAST")
fun <Q: QueryBase<Q>> QueryBase<Q>.where(criteria: Criteria?): Q {
    if (criteria == null) {
        return this as Q
    }
    val predicate = QBooleanField.unwrap(criteria)
    return where(predicate)
}

@Suppress("UNCHECKED_CAST")
fun <Q: QueryBase<Q>> QueryBase<Q>.orderBy(orders: List<Order>): Q {
    if (orders.isEmpty()) {
        return this as Q
    }
    val definitionList = orders.map { order -> QOrder.unwrap(order) }
    val definitionArray: Array<OrderSpecifier<*>> = definitionList.toTypedArray()
    return orderBy(*definitionArray)
}
