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
package com.farcsal.query.dql

import com.farcsal.query.api.Order

internal class DqlOrder(
    private val type: Type,
    private val nullStrategy: NullStrategy,
    private val field: String,
) : Order {

    enum class Type {
        ASC,
        DESC
    }

    enum class NullStrategy {
        FIRST,
        LAST
    }

    override fun toString(): String {
        var definition = ""
        if (Type.DESC == type) {
            definition += '-'
        }
        definition += when (nullStrategy) {
            NullStrategy.FIRST -> if (type == Type.ASC) "nf-" else ""
            NullStrategy.LAST -> if (type == Type.DESC) "nl-" else ""
        }
        definition += field
        return definition
    }

    override fun nullsFirst(): Order {
        return DqlOrder(type, NullStrategy.FIRST, field)
    }

    override fun nullsLast(): Order {
        return DqlOrder(type, NullStrategy.LAST, field)
    }

    override fun equals(other: Any?): Boolean {
        val o = other as? DqlOrder ?: return false
        return o.field == field
    }

    override fun hashCode(): Int {
        return field.hashCode()
    }

}

fun List<Order>.toDql(): String {
    return this.joinToString(separator = ",") { (it as DqlOrder).toString() }
}
