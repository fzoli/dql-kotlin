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
package com.farcsal.query.querydsl

import com.farcsal.query.api.BooleanField
import com.farcsal.query.api.StringField
import com.querydsl.core.types.dsl.SimpleExpression
import com.querydsl.core.types.dsl.StringExpression

class QStringField(private val delegate: StringExpression) : StringField, QExpressionProvider<String> {

    override fun getExpression(): StringExpression {
        return delegate
    }

    override fun equalsIgnoreCase(right: String): BooleanField {
        val q = delegate.equalsIgnoreCase(right)
        return QBooleanField(q)
    }

    override fun contains(right: String): BooleanField {
        return QBooleanField(delegate.contains(right))
    }

    override fun containsIgnoreCase(right: String): BooleanField {
        return QBooleanField(delegate.containsIgnoreCase(right))
    }

    override fun startsWith(right: String): BooleanField {
        return QBooleanField(delegate.startsWith(right))
    }

    override fun startsWithIgnoreCase(right: String): BooleanField {
        return QBooleanField(delegate.startsWithIgnoreCase(right))
    }
    
    override fun isEmpty(): BooleanField {
        return QBooleanField(delegate.isEmpty())
    }
    
    override fun isNotEmpty(): BooleanField {
        return QBooleanField(delegate.isNotEmpty())
    }

    override fun eq(right: String): BooleanField {
        val q = delegate.eq(right)
        return QBooleanField(q)
    }
    
    override fun isNull(): BooleanField {
        return QBooleanField(delegate.isNull())
    }
    
    override fun isNotNull(): BooleanField {
        return QBooleanField(delegate.isNotNull())
    }

    override fun memberOf(right: Collection<String>): BooleanField {
        return QBooleanField(delegate.`in`(right))
    }

}
