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
package com.farcsal.query.querydsl

import com.farcsal.query.api.Order
import com.farcsal.query.api.OrderField
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.ComparableExpressionBase

class QOrderField<E, V>(private val delegate: E) : OrderField
        where E: ComparableExpressionBase<V>,
              E: Path<V>,
              V: Comparable<V> {

    override fun asc(): Order {
        return QOrder(delegate, delegate.asc())
    }

    override fun desc(): Order {
        return QOrder(delegate, delegate.desc())
    }

}
