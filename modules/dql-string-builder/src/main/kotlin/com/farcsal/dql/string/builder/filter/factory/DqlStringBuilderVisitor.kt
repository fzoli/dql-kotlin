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

import com.farcsal.dql.model.DqlNumber
import com.farcsal.dql.model.DqlOp
import com.farcsal.dql.string.builder.filter.DqlStringBuilder

interface DqlStringBuilderVisitor {

    fun not(): DqlStringBuilderVisitor

    fun visit(builder: DqlStringBuilder)

    companion object {

        fun ofOp(
            left: DqlStringBuilderVisitor,
            op: DqlOp,
            right: DqlStringBuilderVisitor
        ): DqlStringBuilderVisitor {
            return DqlStringBuilderVisitorOp(
                left = left,
                operator = op,
                right = right
            )
        }

        fun ofUnary(
            field: String,
            method: String
        ): DqlStringBuilderVisitor {
            return DqlStringBuilderVisitorUnary(
                field = field,
                method = method
            )
        }

        fun ofString(
            field: String,
            method: String,
            value: String
        ): DqlStringBuilderVisitor {
            return DqlStringBuilderVisitorString(
                field = field,
                method = method,
                value = value
            )
        }

        fun ofNumber(
            field: String,
            method: String,
            value: DqlNumber
        ): DqlStringBuilderVisitor {
            return DqlStringBuilderVisitorNumber(
                field = field,
                method = method,
                value = value
            )
        }

        fun ofStringList(
            field: String,
            method: String,
            value: Collection<String>
        ): DqlStringBuilderVisitor {
            return DqlStringBuilderVisitorStringList(
                field = field,
                method = method,
                value = value
            )
        }

        fun ofNumberList(
            field: String,
            method: String,
            value: Collection<DqlNumber>
        ): DqlStringBuilderVisitor {
            return DqlStringBuilderVisitorNumberList(
                field = field,
                method = method,
                value = value
            )
        }

        fun ofVariable(
            field: String,
            method: String,
            value: String
        ): DqlStringBuilderVisitor {
            return DqlStringBuilderVisitorVariable(
                field = field,
                method = method,
                value = value
            )
        }

        fun ofVariableList(
            field: String,
            method: String,
            value: Collection<String>
        ): DqlStringBuilderVisitor {
            return DqlStringBuilderVisitorVariableList(
                field = field,
                method = method,
                value = value
            )
        }

    }

}
