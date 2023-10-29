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
import com.farcsal.query.api.LocalDateField
import java.time.LocalDate

class DqlMethodInvokerLocalDateStrategy(private val expr: LocalDateField) : DqlMethodInvokerStrategy {

    override fun resolveUnaryExpression(field: String, method: String): Criteria {
        return when (method) {
            DqlMethods.IS_NULL -> expr.isNull()
            DqlMethods.IS_NOT_NULL -> expr.isNotNull()
            else -> throw unknownMethodException(field, method, expr)
        }
    }

    override fun resolveNumberExpression(field: String, method: String, number: DqlNumber): Criteria {
        throw unknownMethodException(field, method, expr)
    }

    override fun resolveStringExpression(field: String, method: String, string: String): Criteria {
        val value = LocalDate.parse(string)
        return when (method) {
            DqlMethods.EQ -> expr.eq(value)
            DqlMethods.BEFORE -> expr.before(value)
            DqlMethods.AFTER -> expr.after(value)
            else -> throw unknownMethodException(field, method, expr)
        }
    }

    override fun resolveNumberListExpression(field: String, method: String, numberList: List<DqlNumber>): Criteria {
        throw unknownMethodException(field, method, expr)
    }

    override fun resolveStringListExpression(field: String, method: String, stringList: List<String>): Criteria {
        val value = stringList.map { LocalDate.parse(it) }
        rejectDuplicates(field, value)
        return when (method) {
            DqlMethods.MEMBER_OF -> expr.memberOf(value)
            else -> throw unknownMethodException(field, method, expr)
        }
    }

}
