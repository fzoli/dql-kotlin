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
import com.farcsal.dql.query.parser.sample.order.PersonDqlOrderFieldExpressionResolver
import com.farcsal.dql.query.parser.sample.order.PersonOrderFieldFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DqlOrderFactoryTest {

    @Test
    fun multipleField() {
        val factory = DqlOrderFactory()
        val field = PersonOrderFieldFactory.createPersonField()
        val resolver = PersonDqlOrderFieldExpressionResolver(field)
        val order = "-name.first_name,+name.last_name,age"
        val dsl = "[first_name DESC, last_name ASC, age ASC]"
        val result = factory.create(order, resolver)
        Assertions.assertEquals(dsl, result.toString())
    }

}
