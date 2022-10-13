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

import com.farcsal.dql.query.parser.sample.PersonFields
import com.farcsal.query.api.DefaultOrder
import com.farcsal.query.api.EmbeddedDefaultOrder
import com.farcsal.query.api.OrderField
import com.farcsal.query.api.SerializedField

data class PersonOrderField(
    @field:EmbeddedDefaultOrder
    @field:SerializedField(PersonFields.NAME)
    val name: PersonNameOrderField,

    @field:DefaultOrder(priority = 2)
    @field:SerializedField(PersonFields.AGE)
    val age: OrderField
)
