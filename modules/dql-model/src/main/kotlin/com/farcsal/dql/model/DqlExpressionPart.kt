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
package com.farcsal.dql.model

class DqlExpressionPart internal constructor(
    val type: DqlExpressionPartType,
    val criteria: DqlCriteria? = null,
    val expression: DqlExpression? = null,
    val op: DqlOp? = null
) {
    
    companion object {
        
        fun ofCriteria(value: DqlCriteria): DqlExpressionPart {
            return DqlExpressionPart(
                type = DqlExpressionPartType.CRITERIA,
                criteria = value
            )
        }
        
        fun ofExpression(value: DqlExpression): DqlExpressionPart {
            return DqlExpressionPart(
                type = DqlExpressionPartType.EXPRESSION,
                expression = value
            )
        }

        
        fun ofOp(value: DqlOp): DqlExpressionPart {
            return DqlExpressionPart(
                type = DqlExpressionPartType.OP,
                op = value
            )
        }

    }

    fun requireOp(): DqlOp {
        return op ?: throw NullPointerException()
    }

    fun requireExpression(): DqlExpression {
        return expression ?: throw NullPointerException()
    }

    fun requireCriteria(): DqlCriteria {
        return criteria ?: throw NullPointerException()
    }

    fun visit(visitor: DqlExpressionPartVisitor) {
        when (type) {
            DqlExpressionPartType.CRITERIA ->
                visitor.criteria(criteria ?: throw NullPointerException("criteria"))
            DqlExpressionPartType.EXPRESSION ->
                visitor.expression(expression ?: throw NullPointerException("expression"))
            DqlExpressionPartType.OP ->
                visitor.op(op ?: throw NullPointerException("op"))
        }
    }

    override fun toString(): String {
        return when (type) {
            DqlExpressionPartType.CRITERIA ->
                criteria.toString()
            DqlExpressionPartType.EXPRESSION ->
                expression.toString()
            DqlExpressionPartType.OP ->
                op.toString()
        }
    }

}
