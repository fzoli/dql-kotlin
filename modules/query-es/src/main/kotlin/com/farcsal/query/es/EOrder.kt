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
package com.farcsal.query.es

import com.farcsal.query.api.Order
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.FieldSortBuilder
import org.elasticsearch.search.sort.SortOrder

class EOrder(
    private val fieldName: String,
    private val sortOrder: SortOrder,
    private val nullStrategy: String? = null,
) : Order {

    companion object {
        fun addOrders(builder: SearchSourceBuilder, orders: Collection<Order>?) {
            if (orders.isNullOrEmpty()) {
                return
            }
            for (order in orders) {
                val eo = order as EOrder
                builder.sort(eo.createFieldSortBuilder())
            }
        }
    }

    fun createFieldSortBuilder(): FieldSortBuilder {
        val b = FieldSortBuilder(fieldName)
        b.order(sortOrder)
        if (nullStrategy != null) {
            b.missing(nullStrategy)
        }
        return b
    }

    override fun nullsFirst(): Order {
        return EOrder(fieldName, sortOrder, "_first")
    }

    override fun nullsLast(): Order {
        return EOrder(fieldName, sortOrder, "_last")
    }

    override fun equals(other: Any?): Boolean {
        val o = other as? EOrder ?: return false
        return o.fieldName == fieldName
    }

    override fun hashCode(): Int {
        return fieldName.hashCode()
    }

}
