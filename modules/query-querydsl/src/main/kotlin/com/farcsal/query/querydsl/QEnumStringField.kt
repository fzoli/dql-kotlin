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
import com.farcsal.query.api.EnumField
import com.querydsl.core.types.dsl.StringExpression
import kotlin.reflect.KClass

class QEnumStringField<T : Enum<T>>(
    override val typeClass: KClass<T>,
    private val delegate: StringExpression,
) : EnumField<T>, QExpressionProvider<String> {

    companion object {
        inline fun <reified T: Enum<T>> of(field: StringExpression): QEnumStringField<T> {
            return QEnumStringField(T::class, field)
        }
    }

    override fun getExpression(): StringExpression {
        return delegate
    }

    override fun eq(right: T): BooleanField {
        return QBooleanField(delegate.eq(right.name))
    }

    override fun memberOf(right: Collection<T>): BooleanField {
        return QBooleanField(delegate.`in`(right.map { it.name }))
    }

    override fun isNull(): BooleanField {
        return QBooleanField(delegate.isNull())
    }

    override fun isNotNull(): BooleanField {
        return QBooleanField(delegate.isNotNull())
    }

}
