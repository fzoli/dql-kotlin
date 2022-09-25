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

import com.farcsal.dql.model.DqlMethods
import com.farcsal.dql.model.DqlNumber
import com.farcsal.dql.string.builder.filter.factory.DqlStringBuilderVisitor
import com.farcsal.query.api.Criteria
import com.farcsal.query.api.NumberField

open class DqlNumberField<T>(
    private val field: String,
    private val mapper: (T) -> DqlNumber
): NumberField<T> where T : Number, T : Comparable<T> {

    private fun T.toNumber(): DqlNumber {
        return mapper(this)
    }

    private fun Collection<T>.toNumberList(): List<DqlNumber> {
        return map { it.toNumber() }
    }

    override fun toString(): String {
        return field
    }

    fun gt(right: DqlVariable<T>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.GT, right.name))
    }

    override fun gt(right: T): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofNumber(field, DqlMethods.GT, right.toNumber()))
    }

    fun goe(right: DqlVariable<T>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.GOE, right.name))
    }

    override fun goe(right: T): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofNumber(field, DqlMethods.GOE, right.toNumber()))
    }

    fun loe(right: DqlVariable<T>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.LOE, right.name))
    }

    override fun loe(right: T): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofNumber(field, DqlMethods.LOE, right.toNumber()))
    }

    fun lt(right: DqlVariable<T>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.LT, right.name))
    }

    override fun lt(right: T): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofNumber(field, DqlMethods.LT, right.toNumber()))
    }

    fun eq(right: DqlVariable<T>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.EQ, right.name))
    }

    override fun eq(right: T): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofNumber(field, DqlMethods.EQ, right.toNumber()))
    }

    @JvmName("memberOfVariables")
    fun memberOf(right: Collection<DqlVariable<T>>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariableList(field, DqlMethods.MEMBER_OF, right.map { it.name }))
    }

    override fun memberOf(right: Collection<T>): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofNumberList(field, DqlMethods.MEMBER_OF, right.toNumberList()))
    }

    override fun isNull(): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_NULL))
    }

    override fun isNotNull(): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_NOT_NULL))
    }

}
