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
import com.farcsal.query.api.InstantField
import com.querydsl.core.types.dsl.DateTimeExpression
import java.time.Instant

class QInstantMappedField<E: Comparable<E>>(
    private val delegate: DateTimeExpression<E>,
    private val mapper: (Instant) -> E,
) : InstantField, QExpressionProvider<E> {

    private fun Instant.toMapped(): E {
        return mapper(this)
    }

    override fun getExpression(): DateTimeExpression<E> {
        return delegate
    }

    override fun before(right: Instant): BooleanField {
        return QBooleanField(delegate.before(right.toMapped()))
    }

    override fun after(right: Instant): BooleanField {
        return QBooleanField(delegate.after(right.toMapped()))
    }

    override fun eq(right: Instant): BooleanField {
        return QBooleanField(delegate.eq(right.toMapped()))
    }

    override fun memberOf(right: Collection<Instant>): BooleanField {
        return QBooleanField(delegate.`in`(right.map { it.toMapped() }))
    }

    override fun isNull(): BooleanField {
        return QBooleanField(delegate.isNull())
    }

    override fun isNotNull(): BooleanField {
        return QBooleanField(delegate.isNotNull())
    }

}
