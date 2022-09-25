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
import com.farcsal.query.api.Criteria
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.SimpleExpression

class QBooleanField(private val delegate: BooleanExpression) : BooleanField, QExpressionProvider<Boolean> {
    
    companion object {
        
        fun unwrap(criteria: Criteria): BooleanExpression {
            return (criteria as QBooleanField).delegate
        }
        
    }

    override fun getExpression(): SimpleExpression<Boolean> {
        return delegate
    }

    override fun toString(): String {
        return delegate.toString()
    }

    override fun equals(other: Any?): Boolean {
        return delegate.equals(other)
    }

    override fun hashCode(): Int {
        return delegate.hashCode()
    }

    override operator fun not(): Criteria {
        return QBooleanField(delegate.not())
    }
    
    override fun and(right: Criteria): Criteria {
        val q = right as QBooleanField
        return QBooleanField(delegate.and(q.delegate))
    }
    
    override fun or(right: Criteria): Criteria {
        val q = right as QBooleanField
        return QBooleanField(delegate.or(q.delegate))
    }
    
    override fun isTrue(): BooleanField {
        return QBooleanField(delegate.isTrue())
    }
    
    override fun isFalse(): BooleanField {
        return QBooleanField(delegate.isFalse())
    }
    
    override fun isNull(): BooleanField {
        return QBooleanField(delegate.isNull())
    }
    
    override fun isNotNull(): BooleanField {
        return QBooleanField(delegate.isNotNull())
    }
    
}
