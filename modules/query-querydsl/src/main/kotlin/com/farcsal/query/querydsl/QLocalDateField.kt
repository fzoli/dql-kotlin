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
package com.farcsal.query.querydsl

import com.farcsal.query.api.BooleanField
import com.farcsal.query.api.LocalDateField
import com.querydsl.core.types.dsl.DateExpression
import java.time.LocalDate

class QLocalDateField(private val delegate: DateExpression<LocalDate>) : LocalDateField, QExpressionProvider<LocalDate> {

    override fun getExpression(): DateExpression<LocalDate> {
        return delegate
    }

    override fun before(right: LocalDate): BooleanField {
        return QBooleanField(delegate.before(right))
    }

    override fun after(right: LocalDate): BooleanField {
        return QBooleanField(delegate.after(right))
    }

    override fun eq(right: LocalDate): BooleanField {
        return QBooleanField(delegate.eq(right))
    }

    override fun memberOf(right: Collection<LocalDate>): BooleanField {
        return QBooleanField(delegate.`in`(right.map { it }))
    }

    override fun isNull(): BooleanField {
        return QBooleanField(delegate.isNull())
    }

    override fun isNotNull(): BooleanField {
        return QBooleanField(delegate.isNotNull())
    }

}
