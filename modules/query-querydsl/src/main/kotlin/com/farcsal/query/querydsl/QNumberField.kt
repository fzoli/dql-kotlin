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
import com.farcsal.query.api.NumberField
import com.farcsal.query.api.NumberOperatorField
import com.querydsl.core.types.dsl.NumberExpression

open class QNumberField<T>(
    private val delegate: NumberExpression<T>
) : NumberField<T>, NumberOperatorField<T>, QExpressionProvider<T> where T : Number, T : Comparable<T> {

    override fun getExpression(): NumberExpression<T> {
        return delegate
    }

    override fun gt(right: T): BooleanField {
        return QBooleanField(delegate.gt(right))
    }

    override fun goe(right: T): BooleanField {
        return QBooleanField(delegate.goe(right))
    }

    override fun loe(right: T): BooleanField {
        return QBooleanField(delegate.loe(right))
    }

    override fun lt(right: T): BooleanField {
        return QBooleanField(delegate.lt(right))
    }

    override fun eq(right: T): BooleanField {
        return QBooleanField(delegate.eq(right))
    }

    override fun memberOf(right: Collection<T>): BooleanField {
        return QBooleanField(delegate.`in`(right))
    }

    override fun isNull(): BooleanField {
        return QBooleanField(delegate.isNull())
    }

    override fun isNotNull(): BooleanField {
        return QBooleanField(delegate.isNotNull())
    }

    override fun plus(number: T): NumberOperatorField<T> {
        return QNumberField(delegate.add(number))
    }

    override fun minus(number: T): NumberOperatorField<T> {
        return QNumberField(delegate.subtract(number))
    }

    override fun times(number: T): NumberOperatorField<T> {
        return QNumberField(delegate.multiply(number))
    }

    override fun div(number: T): NumberOperatorField<T> {
        return QNumberField(delegate.divide(number))
    }

}
