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
import com.farcsal.query.api.InstantField
import java.time.Instant

class EInstantField(private val fieldName: String) : InstantField {

    override fun before(right: Instant): Criteria {
        return ECriteria(before(fieldName, right))
    }

    override fun after(right: Instant): Criteria {
        return ECriteria(after(fieldName, right))
    }

    override fun eq(right: Instant): Criteria {
        return ECriteria(equals(fieldName, right))
    }

    override fun memberOf(right: Collection<Instant>): Criteria {
        return ECriteria(or(right.map { number -> equals(fieldName, number) }.toSet()))
    }

    override fun isNull(): Criteria {
        return ECriteria(isNull(fieldName))
    }

    override fun isNotNull(): Criteria {
        return ECriteria(isNotNull(fieldName))
    }

}
