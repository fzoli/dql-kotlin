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
package com.farcsal.query.kt.filter

import com.farcsal.query.api.Criteria
import com.farcsal.query.api.EnumField
import kotlin.reflect.KClass

class KEnumField<T : Enum<T>>(
    val value: T?,
    override val typeClass: KClass<T>,
    override val parserMapper: (T) -> String = { it.name },
) : EnumField<T> {

    override fun eq(right: T): Criteria {
        if (value == null) return KCriteria.of(null)
        return KCriteria.of(value == right)
    }

    override fun memberOf(right: Collection<T>): Criteria {
        if (value == null) return KCriteria.of(null)
        return KCriteria.of(right.contains(value))
    }

    override fun isNull(): Criteria {
        return KCriteria.of(value == null)
    }

    override fun isNotNull(): Criteria {
        return KCriteria.of(value != null)
    }

}
