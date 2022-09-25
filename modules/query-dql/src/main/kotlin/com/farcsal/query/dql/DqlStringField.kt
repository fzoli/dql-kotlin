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
import com.farcsal.query.api.StringField

class DqlStringField(private val field: String) : StringField {

    override fun toString(): String {
        return field
    }

    fun equalsIgnoreCase(right: DqlVariable<String>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.EQUALS_IGNORE_CASE, right.name))
    }

    override fun equalsIgnoreCase(right: String): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofString(field, DqlMethods.EQUALS_IGNORE_CASE, right))
    }

    fun contains(right: DqlVariable<String>): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.CONTAINS, right.name))
    }

    override fun contains(right: String): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofString(field, DqlMethods.CONTAINS, right))
    }

    fun containsIgnoreCase(right: DqlVariable<String>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.CONTAINS_IGNORE_CASE, right.name))
    }

    override fun containsIgnoreCase(right: String): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofString(field, DqlMethods.CONTAINS_IGNORE_CASE, right))
    }

    fun startsWith(right: DqlVariable<String>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.STARTS_WITH, right.name))
    }

    override fun startsWith(right: String): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofString(field, DqlMethods.STARTS_WITH, right))
    }

    fun startsWithIgnoreCase(right: DqlVariable<String>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.STARTS_WITH_IGNORE_CASE, right.name))
    }

    override fun startsWithIgnoreCase(right: String): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofString(field, DqlMethods.STARTS_WITH_IGNORE_CASE, right))
    }

    override fun isEmpty(): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_EMPTY))
    }

    override fun isNotEmpty(): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_NOT_EMPTY))
    }

    fun eq(right: DqlVariable<String>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariable(field, DqlMethods.EQ, right.name))
    }

    override fun eq(right: String): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofString(field, DqlMethods.EQ, right))
    }

    @JvmName("memberOfVariables")
    fun memberOf(right: Collection<DqlVariable<String>>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofVariableList(field, DqlMethods.MEMBER_OF, right.map { it.name }))
    }

    override fun memberOf(right: Collection<String>): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofStringList(field, DqlMethods.MEMBER_OF, right))
    }

    override fun isNull(): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_NULL))
    }

    override fun isNotNull(): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_NOT_NULL))
    }

}
