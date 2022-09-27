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
package com.farcsal.dql.query.parser

import com.farcsal.dql.query.parser.sample.repo.Person
import com.farcsal.dql.query.parser.sample.repo.PersonName
import com.farcsal.dql.query.parser.sample.repo.PersonRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RepositoryTest {

    @Test
    fun repository() {
        val repo = PersonRepository()
        repo.add(Person(name = PersonName(firstName = "User", lastName = "One"), age = 1))
        repo.add(Person(name = PersonName(firstName = "User", lastName = "Two"), age = 1))
        repo.add(Person(name = PersonName(firstName = "User", lastName = "Three"), age = 10))
        val result = repo.list(
            filter = { name.firstName.eq("User").and(age.gt(1)) },
            order = { listOf(age.asc().nullsLast(), name.firstName.desc()) }
        )
        Assertions.assertEquals(1, result.size)
        val person = result.first()
        Assertions.assertEquals("Three", person.name.lastName)
        Assertions.assertEquals(10, person.age)
    }

}
