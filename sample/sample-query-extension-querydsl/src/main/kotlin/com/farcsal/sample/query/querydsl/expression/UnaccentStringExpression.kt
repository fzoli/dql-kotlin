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
package com.farcsal.sample.query.querydsl.expression

import com.querydsl.core.types.Visitor
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringExpression

class UnaccentStringExpression(
    private val delegate: StringExpression,
    private val unaccentFunction: (StringExpression) -> StringExpression
) : StringExpression(delegate) {

    override fun <R, C> accept(v: Visitor<R, C>, context: C?): R? {
        return unaccent().accept(v, context)
    }

    fun unaccent(): StringExpression {
        return unaccentFunction(delegate)
    }

    fun containsIgnoreAccent(str: String): BooleanExpression {
        return unaccent().contains(str.toExpression().unaccent())
    }

    fun containsIgnoreAccentCase(str: String): BooleanExpression {
        return unaccent().containsIgnoreCase(str.toExpression().unaccent())
    }

    private fun String.toExpression(): UnaccentStringExpression {
        return UnaccentStringExpression(Expressions.asString(this), unaccentFunction)
    }

}
