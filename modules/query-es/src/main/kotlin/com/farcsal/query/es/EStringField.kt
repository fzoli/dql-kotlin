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
package com.farcsal.query.es

import com.farcsal.query.api.Criteria
import com.farcsal.query.api.StringField

class EStringField(private val fieldName: String): StringField {

    override fun contains(right: String): Criteria {
        return ECriteria(containsString(fieldName, right))
    }

    override fun containsIgnoreCase(right: String): Criteria {
        return contains(right) // TODO: use ngram (requires template update)
    }

    override fun startsWith(right: String): Criteria {
        return ECriteria(startsWithString(fieldName, right))
    }

    override fun startsWithIgnoreCase(right: String): Criteria {
        return startsWith(right) // TODO: use ngram (requires template update)
    }

    override fun isEmpty(): Criteria {
        return ECriteria(equalsString(fieldName, ""))
    }

    override fun isNotEmpty(): Criteria {
        return isEmpty().not()
    }

    override fun eq(right: String): Criteria {
        return ECriteria(equalsString(fieldName, right))
    }

    override fun equalsIgnoreCase(right: String): Criteria {
        return eq(right) // TODO: use ngram (requires template update)
    }

    override fun memberOf(right: Collection<String>): Criteria {
        return ECriteria(or(right.map { text -> equalsString(fieldName, text) }.toSet()))
    }

    override fun isNull(): Criteria {
        return ECriteria(isNull(fieldName))
    }

    override fun isNotNull(): Criteria {
        return ECriteria(isNotNull(fieldName))
    }

}
