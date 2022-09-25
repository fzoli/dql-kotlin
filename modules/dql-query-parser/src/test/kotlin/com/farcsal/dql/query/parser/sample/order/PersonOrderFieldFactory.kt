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
package com.farcsal.dql.query.parser.sample.order

import com.farcsal.query.querydsl.QOrderField
import com.querydsl.core.types.dsl.Expressions

object PersonOrderFieldFactory {

    private val FIRST_NAME = QOrderField(Expressions.stringPath("first_name"))
    private val LAST_NAME = QOrderField(Expressions.stringPath("last_name"))
    private val AGE = QOrderField(Expressions.numberPath(Long::class.javaObjectType, "age"))

    fun createPersonField(): PersonOrderField {
        return PersonOrderField(
            name = createPersonNameField(),
            age = AGE
        )
    }

    private fun createPersonNameField(): PersonNameOrderField {
        return PersonNameOrderField(
            firstName = FIRST_NAME,
            lastName = LAST_NAME
        )
    }

}
