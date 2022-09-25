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
package com.farcsal.dql.string.builder.filter.factory

import com.farcsal.dql.string.builder.filter.DqlStringBuilder

internal class DqlStringBuilderVisitorVariable(
    private val negated: Boolean = false,
    private val field: String,
    private val method: String,
    private val value: String
) : DqlStringBuilderVisitor {

    override operator fun not(): DqlStringBuilderVisitor {
        return DqlStringBuilderVisitorVariable(
            negated = !negated, field = field, method = method, value = value
        )
    }

    override fun visit(builder: DqlStringBuilder) {
        builder.appendVariableCriteria(negated, field, method, value)
    }

    override fun toString(): String {
        return (if (negated) "!" else "") + "$field.$method({$value})"
    }

}
