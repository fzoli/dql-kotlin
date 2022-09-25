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
import com.farcsal.dql.string.builder.filter.factory.DqlStringBuilderVisitorProvider
import com.farcsal.query.api.BooleanField
import com.farcsal.query.api.Criteria

class DqlBooleanField(private val field: String) : BooleanField, DqlStringBuilderVisitorProvider {

    override fun toString(): String {
        return field
    }

    override fun getDqlStringBuilderVisitor(): DqlStringBuilderVisitor {
        return isTrue().getDqlStringBuilderVisitor()
    }

    override operator fun not(): DqlCriteria {
        return isFalse()
    }

    override fun and(right: Criteria): DqlCriteria {
        return isTrue().and(right)
    }

    override fun or(right: Criteria): DqlCriteria {
        return isTrue().or(right)
    }

    override fun isTrue(): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_TRUE))
    }

    override fun isFalse(): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_FALSE))
    }

    override fun isNull(): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_NULL))
    }

    override fun isNotNull(): DqlCriteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofUnary(field, DqlMethods.IS_NOT_NULL))
    }

}
