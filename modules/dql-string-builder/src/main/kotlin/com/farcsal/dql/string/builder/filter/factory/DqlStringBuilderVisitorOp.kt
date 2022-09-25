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

import com.farcsal.dql.model.DqlOp
import com.farcsal.dql.string.builder.filter.DqlStringBuilder

internal class DqlStringBuilderVisitorOp(
    private val negated: Boolean = false,
    private val left: DqlStringBuilderVisitor,
    private val operator: DqlOp,
    private val right: DqlStringBuilderVisitor
)
: DqlStringBuilderVisitor {

    companion object {
        private val precedences = mapOf(DqlOp.OR to 1, DqlOp.AND to 2)
    }

    private fun precedence(): Int {
        return precedences[operator] ?: error("Unknown OP: $operator")
    }

    private fun hasLowerPrecedence(other: DqlStringBuilderVisitorOp): Boolean {
        return precedence() < other.precedence()
    }

    private fun hasLowerOrEqualPrecedence(other: DqlStringBuilderVisitorOp): Boolean {
        return precedence() <= other.precedence()
    }

    override operator fun not(): DqlStringBuilderVisitor {
        return DqlStringBuilderVisitorOp(
            negated = !negated, left = left, operator = operator, right = right
        )
    }

    override fun visit(builder: DqlStringBuilder) {
        val groupThis = negated
        val groupLeft = left is DqlStringBuilderVisitorOp
                && !(left.negated) && left.hasLowerPrecedence(this)
        val groupRight = right is DqlStringBuilderVisitorOp
                && !(right.negated) && right.hasLowerOrEqualPrecedence(this)
        if (groupThis) {
            builder.beginExpression(negated)
        }
        if (groupLeft) {
            val negatedLeft = (left as DqlStringBuilderVisitorOp).negated
            builder.beginExpression(negatedLeft)
        }
        left.visit(builder)
        if (groupLeft) {
            builder.endExpression()
        }
        builder.appendOp(operator)
        if (groupRight) {
            val negatedRight = (right as DqlStringBuilderVisitorOp).negated
            builder.beginExpression(negatedRight)
        }
        right.visit(builder)
        if (groupRight) {
            builder.endExpression()
        }
        if (groupThis) {
            builder.endExpression()
        }
    }

    override fun toString(): String {
        return (if (negated) "!" else "") + "( $left ${operator.name} $right )"
    }

}
