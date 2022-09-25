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

import com.farcsal.query.api.BooleanField
import com.farcsal.query.api.Criteria

class EBooleanField(private val fieldName: String) : BooleanField {

    override fun not(): Criteria {
        return isFalse()
    }

    override fun and(right: Criteria): Criteria {
        return isTrue().and(right)
    }

    override fun or(right: Criteria): Criteria {
        return isTrue().or(right)
    }

    override fun isNull(): Criteria {
        return ECriteria(isNull(fieldName))
    }

    override fun isNotNull(): Criteria {
        return ECriteria(isNotNull(fieldName))
    }

    override fun isTrue(): Criteria {
        return ECriteria(equalsGeneral(fieldName, true))
    }

    override fun isFalse(): Criteria {
        return ECriteria(equalsGeneral(fieldName, false))
    }

}
