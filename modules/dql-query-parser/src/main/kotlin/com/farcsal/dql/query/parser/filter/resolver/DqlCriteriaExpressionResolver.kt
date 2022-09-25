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
package com.farcsal.dql.query.parser.filter.resolver

import com.farcsal.dql.model.*
import com.farcsal.query.api.Criteria
import java.util.concurrent.atomic.AtomicReference

internal class DqlCriteriaExpressionResolver(private val expressionParser: DqlCriteriaExpressionParser) {

    fun resolve(expression: DqlExpression): Criteria {
        val rawParts = createParts(expression)
        val parts = orderByPrecedence(rawParts)
        return createCriteria(parts, expression)
    }

    private fun createParts(expression: DqlExpression): List<Any> {
        val parts: MutableList<Any> = ArrayList()
        for (part in expression.parts) {
            part.visit(object : DqlExpressionPartVisitor {

                override fun criteria(criteria: DqlCriteria) {
                    val field: String = criteria.field
                    val method: String = criteria.method
                    val expr = AtomicReference<Criteria>()
                    criteria.value.visit(object : DqlCriteriaValueVisitor {
                        override fun unary() {
                            expr.set(expressionParser.resolveUnaryExpression(field, method))
                        }

                        override fun number(number: DqlNumber) {
                            expr.set(expressionParser.resolveNumberExpression(field, method, number))
                        }

                        override fun string(string: String) {
                            expr.set(expressionParser.resolveStringExpression(field, method, string))
                        }

                        override fun numberList(numberList: List<DqlNumber>) {
                            expr.set(expressionParser.resolveNumberListExpression(field, method, numberList))
                        }

                        override fun stringList(stringList: List<String>) {
                            expr.set(expressionParser.resolveStringListExpression(field, method, stringList))
                        }
                    })
                    if (criteria.negated) {
                        expr.set(expr.get().not())
                    }
                    parts.add(expr.get())
                }

                override fun expression(expression: DqlExpression) {
                    parts.add(resolve(expression))
                }

                override fun op(op: DqlOp) {
                    parts.add(op)
                }

            })
        }
        require(parts.isNotEmpty()) { "Empty expression" }
        return parts
    }

    private fun orderByPrecedence(parts: List<Any>): List<Any> {
        // 1 + 2 * 3     = 1 + (2 * 3)
        // 1 * 2 + 3 * 4 = 1 * 2 + (3 * 4)
        // 1 + 2 + 3 * 4 = 1 + 2 + (3 * 4)
        // 1 + 2 * 3 + 4 = 1 + (2 * 3) + 4
        // 1 + 2 * 3 * 4 = 1 + ((2 * 3) * 4)
        // 1 * 2 * 3 + 4 = ok
        val orderedParts = ArrayList(parts)
        var prevOp: DqlOp? = null
        for (i in 1 until parts.size step 2) {
            val op = parts[i] as DqlOp
            if (prevOp != null && op.precedence() > prevOp.precedence()) {
                val left = parts[i-1] as Criteria
                val right = parts[i+1] as Criteria
                val expr = merge(op, left, right)
                orderedParts.removeAt(i + 1)
                orderedParts.removeAt(i)
                orderedParts.removeAt(i - 1)
                orderedParts.add(i - 1, expr)
                return orderByPrecedence(orderedParts)
            }
            prevOp = op
        }
        return parts
    }

    private fun createCriteria(
        parts: List<Any>,
        expression: DqlExpression
    ): Criteria {
        var result: Criteria? = null
        var i = 0
        while (i < parts.size) {
            val part = parts[i]
            if (i == 0) {
                result = part as Criteria
                i += 2
                continue
            }
            val exprLeft = result ?: throw NullPointerException("result")
            val op = parts[i - 1] as DqlOp
            val exprRight = part as Criteria
            result = merge(op, exprLeft, exprRight)
            i += 2
        }
        if (result == null) {
            throw NullPointerException("result")
        }
        return if (expression.negated) {
            result.not()
        } else {
            result
        }
    }

    private fun merge(op: DqlOp, left: Criteria, right: Criteria): Criteria {
        return when (op) {
            DqlOp.OR -> left.or(right)
            DqlOp.AND -> left.and(right)
            else -> throw UnsupportedOperationException("Unhandled OP: $op")
        }
    }

    companion object {

        private val precedences = mapOf(DqlOp.OR to 1, DqlOp.AND to 2)

        private fun DqlOp.precedence(): Int {
            return precedences[this] ?: error("Unknown OP: $this")
        }

    }

}
