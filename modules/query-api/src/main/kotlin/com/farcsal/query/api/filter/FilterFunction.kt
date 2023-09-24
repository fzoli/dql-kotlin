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
package com.farcsal.query.api.filter

import com.farcsal.query.api.Criteria

typealias FilterFunction<T> = T.() -> Criteria?

fun <T> FilterFunction<T>?.evaluate(expression: T): Criteria? {
    return if (this == null) {
        null
    } else {
        this(expression)
    }
}

@JvmName("andOpt")
fun <T> FilterFunction<T>?.and(other: FilterFunction<T>?): FilterFunction<T>? {
    if (this == null) return other
    if (other == null) return this
    return listOf(this, other).merge { l, r -> l.and(r) }
}

@JvmName("and")
fun <T> FilterFunction<T>?.and(other: FilterFunction<T>): FilterFunction<T> {
    if (this == null) return other
    return listOf(this, other).merge { l, r -> l.and(r) }
}

fun <T> List<FilterFunction<T>>.merge(merger: (left: Criteria, right: Criteria) -> Criteria): FilterFunction<T> {
    val list = this
    if (list.isEmpty()) {
        return { null }
    }
    return {
        var c: Criteria? = null
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            val fn = iterator.next()
            if (c == null) {
                c = fn.invoke(this)
            } else {
                val other = fn.invoke(this)
                if (other != null) {
                    c = merger(c, other)
                }
            }
        }
        c
    }
}
