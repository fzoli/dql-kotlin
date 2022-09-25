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
package com.farcsal.query.dql

import com.farcsal.dql.string.builder.filter.factory.DqlExpressionFactory
import com.farcsal.dql.string.builder.filter.factory.DqlStringBuilderVisitor
import com.farcsal.dql.string.builder.filter.factory.DqlStringBuilderVisitorProvider
import com.farcsal.query.api.Criteria
import com.farcsal.dql.model.DqlOp

class DqlCriteria(private val visitor: DqlStringBuilderVisitor) : Criteria, DqlStringBuilderVisitorProvider {

    override fun toString(): String {
        return DqlExpressionFactory.create(this) ?: ""
    }

    override fun getDqlStringBuilderVisitor(): DqlStringBuilderVisitor {
        return visitor
    }

    override operator fun not(): DqlCriteria {
        return DqlCriteria(visitor.not())
    }

    override fun and(right: Criteria): DqlCriteria {
        return DqlCriteria(
            DqlStringBuilderVisitor.ofOp(
                visitor,
                DqlOp.AND,
                cast(right).visitor
            )
        )
    }

    override fun or(right: Criteria): DqlCriteria {
        return DqlCriteria(
            DqlStringBuilderVisitor.ofOp(
                visitor,
                DqlOp.OR,
                cast(right).visitor
            )
        )
    }

    private fun cast(right: Criteria): DqlCriteria {
        var r = right
        if (r is DqlBooleanField) {
            r = r.isTrue()
        }
        return r as DqlCriteria
    }

}

fun Criteria.toDql(): String {
    return (this as DqlCriteria).toString()
}
