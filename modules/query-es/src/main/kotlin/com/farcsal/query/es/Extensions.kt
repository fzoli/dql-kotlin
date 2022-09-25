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
import com.farcsal.query.api.Order
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder

fun SearchSourceBuilder.addOrders(orders: List<Order>) {
    EOrder.addOrders(this, orders)
}

fun SearchSourceBuilder.addFilter(criteria: Criteria) {
    val filterQueryBuilder = ECriteria.unwrap(criteria)
    val queryBuilder = QueryBuilders.boolQuery().must(filterQueryBuilder)
    if (queryBuilder.hasClauses()) {
        this.query(queryBuilder)
    }
}
