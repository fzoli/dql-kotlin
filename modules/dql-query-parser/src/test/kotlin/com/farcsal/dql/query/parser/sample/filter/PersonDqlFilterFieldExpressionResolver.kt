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
package com.farcsal.dql.query.parser.sample.filter

import com.farcsal.dql.query.parser.filter.resolver.DqlFilterFieldExpressionResolver
import com.farcsal.dql.query.parser.util.asEmbeddedField
import com.farcsal.dql.query.parser.util.isEmbeddedField
import com.farcsal.dql.query.parser.util.unknownFieldException
import com.farcsal.dql.query.parser.sample.PersonFields
import com.farcsal.query.api.Field

class PersonDqlFilterFieldExpressionResolver(private val f: PersonFilterField) :
    DqlFilterFieldExpressionResolver {

    private val personNameResolver = lazy {
        PersonNameDqlFilterFieldExpressionResolver(
            f.name
        )
    }

    override fun getExpression(field: String): Field {
        if (isEmbeddedField(field, PersonFields.NAME)) {
            val embeddedField =
                asEmbeddedField(field, PersonFields.NAME)
            return personNameResolver.value.getExpression(embeddedField)
        }
        return when (field) {
            PersonFields.AGE -> f.age
            else -> throw unknownFieldException(field)
        }
    }

}
