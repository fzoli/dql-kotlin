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
package com.farcsal.query.querydsl

import com.farcsal.query.api.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Path

class QOrder(private val path: Path<*>, private val delegate: OrderSpecifier<*>) : Order {

    companion object {

        fun unwrap(order: Order): OrderSpecifier<*> {
            return (order as QOrder).delegate
        }

    }

    override fun nullsFirst(): Order {
        return QOrder(path, delegate.nullsFirst())
    }

    override fun nullsLast(): Order {
        return QOrder(path, delegate.nullsLast())
    }

    override fun toString(): String {
        return delegate.toString()
    }

    override fun equals(other: Any?): Boolean {
        val o = other as? QOrder ?: return false
        return path == o.path
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

}
