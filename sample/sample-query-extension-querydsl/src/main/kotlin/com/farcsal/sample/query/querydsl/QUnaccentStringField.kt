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
package com.farcsal.sample.query.querydsl

import com.farcsal.query.api.Criteria
import com.farcsal.query.api.StringField
import com.farcsal.query.querydsl.QBooleanField
import com.farcsal.query.querydsl.QExpressionProvider
import com.farcsal.query.querydsl.QStringField
import com.farcsal.sample.query.api.UnaccentStringField
import com.farcsal.sample.query.querydsl.expression.UnaccentStringExpression
import com.querydsl.core.types.dsl.StringExpression

class QUnaccentStringField(private val delegate: QStringField) : UnaccentStringField, StringField by delegate, QExpressionProvider<String> {

    override fun getExpression(): StringExpression {
        return delegate.getExpression()
    }

    override fun containsIgnoreAccent(right: String): Criteria {
        return QBooleanField(unaccentDelegate().containsIgnoreAccent(right))
    }

    override fun containsIgnoreAccentCase(right: String): Criteria {
        return QBooleanField(unaccentDelegate().containsIgnoreAccentCase(right))
    }

    private fun unaccentDelegate(): UnaccentStringExpression {
        val expr = getExpression()
        if (expr is UnaccentStringExpression) {
            return expr
        }
        throw UnsupportedOperationException("Not an unaccent string field")
    }

}
