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
import com.farcsal.dql.string.builder.filter.factory.DqlStringBuilderVisitor
import com.farcsal.query.api.Criteria
import com.farcsal.query.api.InstantField
import java.time.Instant

class DqlInstantField(private val field: String): InstantField {

    override fun toString(): String {
        return field
    }

    fun before(right: DqlVariable<Instant>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.BEFORE, right.name))
    }

    override fun before(right: Instant): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofString(field, DqlMethods.BEFORE, right.toString()))
    }

    fun after(right: DqlVariable<Instant>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.AFTER, right.name))
    }

    override fun after(right: Instant): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofString(field, DqlMethods.AFTER, right.toString()))
    }

    fun eq(right: DqlVariable<Instant>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.EQ, right.name))
    }

    override fun eq(right: Instant): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofString(field, DqlMethods.EQ, right.toString()))
    }

    @JvmName("memberOfVariables")
    fun memberOf(right: Collection<DqlVariable<Instant>>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariableList(field, DqlMethods.MEMBER_OF, right.map { it.name }))
    }

    override fun memberOf(right: Collection<Instant>): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofStringList(field, DqlMethods.MEMBER_OF, right.map { it.toString() }))
    }

    override fun isNull(): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_NULL))
    }

    override fun isNotNull(): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_NOT_NULL))
    }

}
