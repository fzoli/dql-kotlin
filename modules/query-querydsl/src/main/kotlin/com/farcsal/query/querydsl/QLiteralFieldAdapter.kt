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
import com.querydsl.core.types.dsl.SimpleExpression

class QLiteralFieldAdapter<I, O>(
    private val delegate: SimpleExpression<I>,
    private val mapper: (O) -> I
) : LiteralField<O>, QExpressionProvider<I> {
    
    override fun getExpression(): SimpleExpression<I> {
        return delegate
    }
    
    private fun map(right: O): I {
        return mapper(right)
    }
    
    private fun map(right: Collection<O>): Collection<I> {
        return right.map { item: O -> this.map(item) }
    }
    
    override fun eq(right: O): Criteria {
        return QBooleanField(delegate.eq(map(right)))
    }

    override fun memberOf(right: Collection<O>): Criteria {
        return QBooleanField(delegate.`in`(map(right)))
    }

    override fun isNull(): Criteria {
        return QBooleanField(delegate.isNull())
    }

    override fun isNotNull(): Criteria {
        return QBooleanField(delegate.isNotNull())
    }

}
