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
package com.farcsal.sample.repository.api.user.model

import com.farcsal.query.api.*
import com.farcsal.sample.query.api.UnaccentStringField
import com.farcsal.sample.repository.api.util.phonenumber.model.PhoneNumberFilterField

class UserFilterField(
    @field:SerializedField(UserFields.ID)
    val id: UuidField,
    @field:SerializedField(UserFields.CREATION_TIME)
    val creationTime: InstantField,
    @field:SerializedField(UserFields.LEVEL)
    val level: IntField,
    @field:SerializedField(UserFields.NAME)
    val name: UnaccentStringField,
    @field:SerializedField(UserFields.EMAIL_ADDRESS)
    val emailAddress: StringField,
    @field:SerializedField(UserFields.PHONE_NUMBERS)
    val phoneNumbers: SetField<PhoneNumberFilterField>,
)
