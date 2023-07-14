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
package com.farcsal.datatype

import kotlin.reflect.KClass

fun <T> String.split(separator: String, parser: (String) -> T): List<T> {
    if (isEmpty()) {
        return listOf()
    }
    return split(separator).map { text ->
        parser(text)
    }
}

fun <T : Enum<T>> String.toEnum(type: KClass<T>, mapper: (T) -> String = { it.name }): T {
    val result = type.java.enumConstants.first { constant ->
        this == mapper(constant)
    }
    return requireNotNull(result) { "Invalid name '${this}' for enum '$type'" }
}

inline fun <reified T : Enum<T>> String.toEnum(noinline mapper: (T) -> String = { it.name }): T {
    return toEnum(T::class, mapper)
}
