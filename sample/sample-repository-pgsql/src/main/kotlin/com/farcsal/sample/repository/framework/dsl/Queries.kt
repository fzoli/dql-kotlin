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
package com.farcsal.sample.repository.framework.dsl

import com.farcsal.sample.repository.api.util.Paging
import com.querydsl.core.support.QueryBase

@Suppress("UNCHECKED_CAST")
fun <Q: QueryBase<Q>> QueryBase<Q>.pageBy(paging: Paging?): Q {
    var query = this as Q
    if (paging == null) {
        return query
    }
    val offset = paging.offset
    val limit = paging.limit
    if (offset != null) {
        query = query.offset(offset)
    }
    if (limit != null) {
        query = query.limit(limit.toLong())
    }
    return query
}
