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
package com.farcsal.sample.query.dql

import com.farcsal.dql.string.builder.filter.factory.DqlStringBuilderVisitor
import com.farcsal.query.api.Criteria
import com.farcsal.query.api.StringField
import com.farcsal.query.dql.DqlCriteria
import com.farcsal.query.dql.DqlStringField
import com.farcsal.sample.query.api.DqlExtendedMethods
import com.farcsal.sample.query.api.UnaccentStringField

class DqlUnaccentStringField(private val delegate: DqlStringField): UnaccentStringField, StringField by delegate {

    override fun containsIgnoreAccent(right: String): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofString(delegate.toString(), DqlExtendedMethods.CONTAINS_IGNORE_ACCENT, right))
    }

    override fun containsIgnoreAccentCase(right: String): Criteria {
        return DqlCriteria(DqlStringBuilderVisitor.ofString(delegate.toString(), DqlExtendedMethods.CONTAINS_IGNORE_ACCENT_CASE, right))
    }

}
