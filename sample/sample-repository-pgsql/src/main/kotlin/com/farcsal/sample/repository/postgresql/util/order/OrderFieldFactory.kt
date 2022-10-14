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
package com.farcsal.sample.repository.postgresql.util.order

import com.farcsal.query.api.OrderField
import com.farcsal.query.api.StringOrderField
import com.farcsal.query.querydsl.QOrderField
import com.farcsal.query.querydsl.QStringOrderField
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.ComparableExpressionBase
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringExpression
import com.querydsl.core.types.dsl.StringPath
import java.util.*

fun <E, V> E.toOrderField(): OrderField
    where E: ComparableExpressionBase<V>,
          E: Path<V>,
          V: Comparable<V> {
    return QOrderField(this)
}

fun StringPath.toStringOrderField(): StringOrderField {
    return QStringOrderField(this) { locale -> createStringOrderTemplate(locale, this) }
}

private fun createStringOrderTemplate(
    locale: Locale?,
    expression: StringExpression
): StringExpression {
    if (locale == null) {
        return expression
    }
    val collation = localeText(locale)
    // NOTE: collate does not support parameter binding, but it is not a security problem, because the locale code is
    // managed by the server internally and the server accepts only the supported ones.
    return Expressions.stringTemplate("{0} COLLATE \"$collation\"", expression)
}

private fun localeText(locale: Locale): String {
    return locale.language + "_" + locale.country
}
