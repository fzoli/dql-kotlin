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
package com.farcsal.query.api

/**
 * Reflection-based helper annotation.
 *
 * Preferred to use on fields with [OrderField] type like this: @field:DefaultOrder(DefaultOrder.Direction.ASC)
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class DefaultOrder(
    val direction: Direction = Direction.ASC,
    val nullHandling: NullHandling = NullHandling.NULLS_LAST,
    val priority: Int = 0,
) {
    enum class Direction {
        ASC,
        DESC,
    }
    enum class NullHandling {
        NULLS_FIRST,
        NULLS_LAST,
    }
}
