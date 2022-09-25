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
import com.farcsal.query.api.StringField

class KStringField(val value: String?) : StringField {

    override fun equalsIgnoreCase(right: String): Criteria {
        if (value == null) return KCriteria.of(null)
        return KCriteria.of(value.equals(right, ignoreCase = true))
    }

    override fun contains(right: String): Criteria {
        if (value == null) return KCriteria.of(null)
        return KCriteria.of(value.contains(right))
    }

    override fun containsIgnoreCase(right: String): Criteria {
        if (value == null) return KCriteria.of(null)
        return KCriteria.of(value.contains(right, ignoreCase = true))
    }

    override fun startsWith(right: String): Criteria {
        if (value == null) return KCriteria.of(null)
        return KCriteria.of(value.startsWith(right))
    }

    override fun startsWithIgnoreCase(right: String): Criteria {
        if (value == null) return KCriteria.of(null)
        return KCriteria.of(value.startsWith(right, ignoreCase = true))
    }
    
    override fun isEmpty(): Criteria {
        if (value == null) return KCriteria.of(null)
        return KCriteria.of(value.isEmpty())
    }
    
    override fun isNotEmpty(): Criteria {
        if (value == null) return KCriteria.of(null)
        return KCriteria.of(value.isNotEmpty())
    }

    override fun eq(right: String): Criteria {
        if (value == null) return KCriteria.of(null)
        return KCriteria.of(value == right)
    }
    
    override fun isNull(): Criteria {
        return KCriteria.of(value == null)
    }
    
    override fun isNotNull(): Criteria {
        return KCriteria.of(value != null)
    }

    override fun memberOf(right: Collection<String>): Criteria {
        if (value == null) return KCriteria.of(null)
        return KCriteria.of(right.contains(value))
    }

}
