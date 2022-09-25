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
package com.farcsal.sample.testengine.context.time

@JvmInline
value class Millisecond(val value: Long)

val Long.ms: Millisecond
    get() = Millisecond(this)

val Int.ms: Millisecond
    get() = Millisecond(this.toLong())

@JvmInline
value class Second(val value: Long)

val Long.second: Second
    get() = Second(this)

val Int.second: Second
    get() = Second(this.toLong())

@JvmInline
value class Day(val value: Long)

val Long.day: Day
    get() = Day(this)

val Int.day: Day
    get() = Day(this.toLong())
