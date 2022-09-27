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
package com.farcsal.dql.query.parser.sample.repo

import com.farcsal.dql.query.parser.sample.filter.PersonFilterField
import com.farcsal.dql.query.parser.sample.filter.PersonNameFilterField
import com.farcsal.query.kt.filter.KLongField
import com.farcsal.query.kt.filter.KStringField

fun Person.toFilterField(): PersonFilterField {
    return PersonFilterField(
        name = name.toFilterField(),
        age = KLongField(age),
    )
}

fun PersonName.toFilterField(): PersonNameFilterField {
    return PersonNameFilterField(
        firstName = KStringField(firstName),
        lastName = KStringField(lastName)
    )
}
