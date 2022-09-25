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

import com.farcsal.dql.query.parser.filter.DqlFilterFactory
import com.farcsal.dql.query.parser.filter.field.DynamicDqlFilterFieldParser
import com.farcsal.dql.query.parser.sample.filter.PersonFilterField
import com.farcsal.dql.query.parser.sample.filter.PersonFilterFieldFactory
import com.farcsal.query.api.filter.evaluate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DynamicDqlFilterFieldParserTest {

    @Test
    fun filterParser() {
        val factory = DqlFilterFactory()
        val parser = DynamicDqlFilterFieldParser(factory)
        val field = PersonFilterFieldFactory.createPersonField()

        val filter = "!(name.first_name:eq:\"a\"&name.last_name:memberOf:[\"b\"])|!(age:gt:20)"
        val filterFn = parser.parseFilter<PersonFilterField>(filter)
        val result = filterFn.evaluate(field)

        val dsl = "!(first_name = a && last_name = b) || !(age > 20)"
        Assertions.assertEquals(dsl, result.toString())
    }

}
