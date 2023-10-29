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
import com.farcsal.sample.repository.postgresql.util.mapper.toLocalDateTime
import com.querydsl.core.types.dsl.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

fun <T> T.toSetField(): SetField<T> {
    return QSetField(this)
}

fun SimpleExpression<UUID>.toUuidField(): UuidField {
    return QUuidField.of(this)
}

fun DateTimeExpression<LocalDateTime>.toInstantField(): InstantField {
    return QInstantMappedField(this) { it.toLocalDateTime() }
}

fun DateExpression<LocalDate>.toLocalDateField(): LocalDateField {
    return QLocalDateField(this)
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

inline fun <reified T: Enum<T>> StringExpression.toEnumField(
    noinline mapper: (T) -> String = { it.name },
    noinline parserMapper: (T) -> String = mapper,
): EnumField<T> {
    return QEnumStringField.of(this, mapper, parserMapper)
}
