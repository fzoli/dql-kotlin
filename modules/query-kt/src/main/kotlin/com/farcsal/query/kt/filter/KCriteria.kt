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

class KCriteria private constructor(val value: Boolean?) : Criteria {

    companion object {

        private val TRUE = KCriteria(true)
        private val FALSE = KCriteria(false)
        private val NULL = KCriteria(null)

        fun unwrap(criteria: Criteria): Boolean {
            return (criteria as KCriteria).value ?: false
        }

        fun of(value: Boolean?): Criteria {
            if (value == null) {
                return NULL
            }
            return if (value) TRUE else FALSE
        }

    }

    override fun not(): Criteria {
        if (value == null) {
            return NULL
        }
        return of(!value)
    }

    override fun and(right: Criteria): Criteria {
        val rightValue = (right as KCriteria).value
        if (false == value || false == rightValue) {
            return FALSE
        }
        if (null == value || null == rightValue) {
            return NULL
        }
        return of(value && rightValue)
    }

    override fun or(right: Criteria): Criteria {
        val rightValue = (right as KCriteria).value
        if (true == value || true == rightValue) {
            return TRUE
        }
        if (null == value || null == rightValue) {
            return NULL
        }
        return of(value || rightValue)
    }

}
