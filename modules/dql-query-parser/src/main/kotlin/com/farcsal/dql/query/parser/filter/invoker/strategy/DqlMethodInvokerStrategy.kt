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

import com.farcsal.datatype.findDuplicates
import com.farcsal.dql.query.parser.filter.resolver.DqlCriteriaExpressionParser
import com.farcsal.query.api.Field
import java.util.stream.Collectors

interface DqlMethodInvokerStrategy : DqlCriteriaExpressionParser {

    fun unknownMethodException(
        field: String,
        method: String,
        expr: Field
    ): UnsupportedOperationException {
        return UnsupportedOperationException(
            "Unknown method '" + method + "' for " + expr.javaClass.simpleName + " field: " + field
        )
    }

    fun <T> rejectDuplicates(
        field: String,
        collection: Collection<T>
    ) {
        val duplicates = collection.findDuplicates()
        if (duplicates.isEmpty()) {
            return
        }
        val values: String = duplicates.stream()
            .map { obj: T -> obj.toString() }
            .collect(Collectors.joining(",", "[", "]"))
        throw IllegalArgumentException("Field '$field' has duplicates: $values")
    }

}
