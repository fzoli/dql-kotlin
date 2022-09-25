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
package com.farcsal.dql.query.parser.filter.invoker.strategy

import com.farcsal.dql.model.DqlMethods
import com.farcsal.dql.model.DqlNumber
import com.farcsal.query.api.Criteria
import com.farcsal.query.api.LongField

class DqlMethodInvokerLongStrategy(private val expr: LongField) : DqlMethodInvokerStrategy {

    override fun resolveUnaryExpression(field: String, method: String): Criteria {
        return when (method) {
            DqlMethods.IS_NULL -> expr.isNull()
            DqlMethods.IS_NOT_NULL -> expr.isNotNull()
            else -> throw unknownMethodException(field, method, expr)
        }
    }

    override fun resolveNumberExpression(field: String, method: String, number: DqlNumber): Criteria {
        val value = toLong(number)
        return when (method) {
            DqlMethods.EQ -> expr.eq(value)
            DqlMethods.GT -> expr.gt(value)
            DqlMethods.GOE -> expr.goe(value)
            DqlMethods.LOE -> expr.loe(value)
            DqlMethods.LT -> expr.lt(value)
            else -> throw unknownMethodException(field, method, expr)
        }
    }

    override fun resolveStringExpression(field: String, method: String, string: String): Criteria {
        throw unknownMethodException(field, method, expr)
    }

    override fun resolveNumberListExpression(field: String, method: String, numberList: List<DqlNumber>): Criteria {
        val value = toLongList(numberList)
        rejectDuplicates(field, value)
        return when (method) {
            DqlMethods.MEMBER_OF -> expr.memberOf(value)
            else -> throw unknownMethodException(field, method, expr)
        }
    }

    override fun resolveStringListExpression(field: String, method: String, stringList: List<String>): Criteria {
        throw unknownMethodException(field, method, expr)
    }

    private fun toLong(number: DqlNumber): Long {
        return number.toLong()
    }

    private fun toLongList(numberList: List<DqlNumber>): List<Long> {
        return numberList.map { number -> this.toLong(number) }
    }

}
