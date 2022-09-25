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

import com.farcsal.dql.query.parser.order.DqlOrderFactory
import com.farcsal.dql.query.parser.order.OrderFunctionFactory
import com.farcsal.dql.query.parser.order.field.DynamicDqlOrderFieldParser
import com.farcsal.dql.query.parser.sample.order.PersonOrderField
import com.farcsal.dql.query.parser.sample.order.PersonOrderFieldFactory
import com.farcsal.query.api.order.evaluate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DynamicDqlOrderFieldParserTest {

    @Test
    fun orderParser() {
        val factory = DqlOrderFactory()
        val fnFactory = OrderFunctionFactory(factory)
        val parser = DynamicDqlOrderFieldParser(fnFactory)
        val field = PersonOrderFieldFactory.createPersonField()

        val order = "-name.first_name,+name.last_name,age"
        val orderFn = parser.parseOrder<PersonOrderField>(order)
        val result = orderFn.evaluate(field)

        val dsl = "[first_name DESC, last_name ASC, age ASC]"
        Assertions.assertEquals(dsl, result.toString())
    }

}
