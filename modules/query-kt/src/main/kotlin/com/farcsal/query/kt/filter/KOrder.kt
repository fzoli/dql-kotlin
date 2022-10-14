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

import com.farcsal.query.api.Order

class KOrder<T : Any>(private val field: KOrderField<*>, val comparator: Comparator<in T>) : Order {

    override fun nullsFirst(): Order {
        return KOrder(field, nullsFirst(comparator))
    }

    override fun nullsLast(): Order {
        return KOrder(field, nullsLast(comparator))
    }

    override fun equals(other: Any?): Boolean {
        val o = other as? KOrder<*> ?: return false
        return o.field === field
    }

    override fun hashCode(): Int {
        return field.hashCode()
    }

}
