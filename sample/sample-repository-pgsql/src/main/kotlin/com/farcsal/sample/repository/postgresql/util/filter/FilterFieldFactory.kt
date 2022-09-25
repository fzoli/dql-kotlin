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
package com.farcsal.sample.repository.postgresql.util.filter

import com.farcsal.query.api.*
import com.farcsal.query.querydsl.*
import com.farcsal.sample.query.api.UnaccentStringField
import com.farcsal.sample.query.querydsl.QUnaccentStringField
import com.querydsl.core.types.dsl.DateTimeExpression
import com.querydsl.core.types.dsl.NumberExpression
import com.querydsl.core.types.dsl.SimpleExpression
import com.querydsl.core.types.dsl.StringExpression
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

private val timeZone = ZoneId.of("UTC")

fun <T> T.toSetField(): SetField<T> {
    return QSetField(this)
}

fun SimpleExpression<UUID>.toUuidField(): UuidField {
    return QUuidField.of(this)
}

fun DateTimeExpression<LocalDateTime>.toInstantField(): InstantField {
    return QInstantMappedField(this) { LocalDateTime.ofInstant(it, timeZone) }
}

fun NumberExpression<Int>.toIntField(): IntField {
    return QIntField(this)
}

fun StringExpression.toStringField(): StringField {
    return QStringField(this)
}

fun StringExpression.toUnaccentStringField(): UnaccentStringField {
    return QUnaccentStringField(QStringField(this.toUnaccent()))
}

inline fun <reified T: Enum<T>> StringExpression.toEnumField(): EnumField<T> {
    return QEnumStringField.of(this)
}
