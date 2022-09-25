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
import com.farcsal.query.api.filter.FilterFunction
import com.farcsal.query.kt.filter.invokeFilter

class PersonRepository {

    private val items = mutableListOf<Person>()

    fun list(filter: FilterFunction<PersonFilterField>? = null): List<Person> {
        return items.invokeFilter(filter) { it.toFilterField() }
    }

    fun add(person: Person) {
        items.add(person)
    }

}
