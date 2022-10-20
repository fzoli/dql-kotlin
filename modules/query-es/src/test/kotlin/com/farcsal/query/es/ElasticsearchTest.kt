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

import org.elasticsearch.search.builder.SearchSourceBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ElasticsearchTest {

    @Test
    fun elasticQuery() {
        val age = ELongField("age")
        val criteria = age.gt(10).and(age.lt(100))
        val orders = listOf(EOrderField("age").asc())

        val sourceBuilder = SearchSourceBuilder()
        sourceBuilder.addFilter(criteria)
        sourceBuilder.addOrders(orders)

        Assertions.assertEquals("{\"query\":{\"bool\":{\"must\":[{\"bool\":{\"must\":[{\"range\":{\"age\":{\"gt\":10,\"boost\":1.0}}},{\"range\":{\"age\":{\"lt\":100,\"boost\":1.0}}}],\"boost\":1.0}}],\"boost\":1.0}},\"sort\":[{\"age\":{\"order\":\"asc\"}}]}", sourceBuilder.toString())
    }

}
