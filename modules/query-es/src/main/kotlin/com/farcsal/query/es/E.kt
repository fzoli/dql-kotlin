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

import org.apache.lucene.queryparser.classic.QueryParser
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import java.time.Instant

private fun escapeString(value: String): String {
    return QueryParser.escape(value)
}

fun not(builder: QueryBuilder): QueryBuilder {
    return QueryBuilders.boolQuery().mustNot(builder)
}

fun and(a: QueryBuilder, b: QueryBuilder): QueryBuilder {
    return QueryBuilders.boolQuery().must(a).must(b)
}

fun or(a: QueryBuilder, b: QueryBuilder): QueryBuilder {
    return QueryBuilders.boolQuery().should(a).should(b)
}

fun and(builders: Collection<QueryBuilder>): QueryBuilder {
    require(!builders.isEmpty()) { "Empty collection" }
    val b = QueryBuilders.boolQuery()
    for (builder in builders) {
        b.must(builder)
    }
    return b
}

fun or(builders: Collection<QueryBuilder>): QueryBuilder {
    require(!builders.isEmpty()) { "Empty collection" }
    val b = QueryBuilders.boolQuery()
    for (builder in builders) {
        b.should(builder)
    }
    return b
}

fun isNull(fieldName: String): QueryBuilder {
    return QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(fieldName))
}

fun isNotNull(fieldName: String): QueryBuilder {
    return QueryBuilders.existsQuery(fieldName)
}

fun equalsGeneral(fieldName: String, value: Any): QueryBuilder {
    return QueryBuilders.termQuery(fieldName, value)
}

fun equalsString(fieldName: String, value: String): QueryBuilder {
    return QueryBuilders.matchQuery(fieldName, escapeString(value))
}

fun containsString(fieldName: String, value: String): QueryBuilder {
    return QueryBuilders.wildcardQuery(fieldName, "*" + escapeString(value) + "*")
}

fun startsWithString(fieldName: String, value: String): QueryBuilder {
    return QueryBuilders.prefixQuery(fieldName, escapeString(value))
}

fun greaterThanGeneral(fieldName: String, value: Any): QueryBuilder {
    return QueryBuilders.rangeQuery(fieldName).gt(value)
}

fun greaterThanOrEqualGeneral(fieldName: String, value: Any): QueryBuilder {
    return QueryBuilders.rangeQuery(fieldName).gte(value)
}

fun lessThanGeneral(fieldName: String, value: Any): QueryBuilder {
    return QueryBuilders.rangeQuery(fieldName).lt(value)
}

fun lessThanOrEqualGeneral(fieldName: String, value: Any): QueryBuilder {
    return QueryBuilders.rangeQuery(fieldName).lte(value)
}

fun before(fieldName: String, value: Instant): QueryBuilder {
    return QueryBuilders.rangeQuery(fieldName).lt(value.toString())
}

fun after(fieldName: String, value: Instant): QueryBuilder {
    return QueryBuilders.rangeQuery(fieldName).gt(value.toString())
}

fun equals(fieldName: String, value: Instant): QueryBuilder {
    return QueryBuilders.termQuery(fieldName, value.toString())
}
