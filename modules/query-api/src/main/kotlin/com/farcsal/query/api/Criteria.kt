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

interface Criteria {

    // NOTE:
    // '|' and '&' have a decent chance of becoming overload operators in Kotlin at some point.
    // For now `and` and `or` are infix functions.
    // Note that infix functions have the same precedence so 'a or b and c' is translated to '(a or b) and c'.
    // However, operators will have the expected precedence so `a | b & c` will be translated to `a or (b and c)`.
    // Infix functions will not conflict with operator functions because we will be able to write this:
    // 'infix operator fun and(right: Criteria): Criteria'

    // NOTE:
    // Field functions could be infix ones, but it would cause more problems.
    // For example, with infix functions you must write "age gt 1 or (age gt 2)"
    // because "age gt 1 or age gt 2" is translated to an invalid expression: "((age gt 1) or age) gt 2".
    // Easier to write "age.gt(1) or age.gt(2)".

    operator fun not(): Criteria

    infix fun and(right: Criteria): Criteria

    infix fun or(right: Criteria): Criteria

}
