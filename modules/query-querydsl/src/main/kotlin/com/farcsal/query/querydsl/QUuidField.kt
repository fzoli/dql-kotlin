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

import com.farcsal.query.api.Criteria
import com.farcsal.query.api.LiteralField
import com.farcsal.query.api.UuidField
import com.querydsl.core.types.dsl.SimpleExpression
import java.util.*

class QUuidField private constructor(private val delegate: LiteralField<UUID>) : UuidField, QExpressionProvider<UUID> {

    companion object {

        fun of(
            delegate: SimpleExpression<UUID>
        ): QUuidField {
            return QUuidField(QLiteralFieldAdapter(delegate) {it})
        }

        fun <T> of(
            delegate: SimpleExpression<T>,
            mapper: (UUID) -> T
        ): QUuidField {
            return QUuidField(QLiteralFieldAdapter(delegate, mapper))
        }

    }

    @Suppress("UNCHECKED_CAST")
    override fun getExpression(): SimpleExpression<UUID> {
        return (delegate as QExpressionProvider<UUID>).getExpression()
    }

    override fun eq(right: UUID): Criteria {
        return delegate.eq(right)
    }

    override fun memberOf(right: Collection<UUID>): Criteria {
        return delegate.memberOf(right)
    }

    override fun isNull(): Criteria {
        return delegate.isNull()
    }

    override fun isNotNull(): Criteria {
        return delegate.isNotNull()
    }

}
