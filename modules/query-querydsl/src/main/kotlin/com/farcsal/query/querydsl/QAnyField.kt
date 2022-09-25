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

import com.farcsal.query.api.AnyField
import com.farcsal.query.api.Criteria
import com.farcsal.query.api.LiteralField
import kotlin.reflect.KClass

class QAnyField<T: Any>(
    override val backingField: LiteralField<T>,
    private val clazz: KClass<T>
) : AnyField {

    override fun eq(right: Any): Criteria {
        return backingField.eq(right.cast())
    }

    override fun memberOf(right: Collection<Any>): Criteria {
        return backingField.memberOf(right.map { it.cast() })
    }

    override fun isNull(): Criteria {
        return backingField.isNull()
    }

    override fun isNotNull(): Criteria {
        return backingField.isNotNull()
    }

    @Suppress("UNCHECKED_CAST")
    private fun Any.cast(): T {
        try {
            return this@cast as T
        } catch (ex: Exception) {
            throw IllegalArgumentException("Type mismatch: expected ${clazz.java} got ${this@cast.javaClass}")
        }
    }

}

inline fun <reified T: Any> LiteralField<T>.toAny(): QAnyField<T> {
    return QAnyField(this, T::class)
}
