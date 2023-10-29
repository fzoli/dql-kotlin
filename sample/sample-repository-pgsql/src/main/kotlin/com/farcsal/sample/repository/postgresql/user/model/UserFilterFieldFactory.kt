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
package com.farcsal.sample.repository.postgresql.user.model

import com.farcsal.sample.repository.api.user.model.UserFilterField
import com.farcsal.sample.repository.api.util.ids.asUserId
import com.farcsal.sample.repository.api.util.phonenumber.model.PhoneNumberFilterField
import com.farcsal.sample.repository.postgresql.dsl.QUser
import com.farcsal.sample.repository.postgresql.dsl.QUserPhoneNumber
import com.farcsal.sample.repository.postgresql.util.filter.*

internal fun createFilterField(user: QUser, userPhoneNumber: QUserPhoneNumber): UserFilterField {
    return UserFilterField(
        id = user.id.toUuidField().asUserId(),
        creationTime = user.creationTime.toInstantField(),
        level = user.level.toIntField(),
        name = user.name.toUnaccentStringField(),
        birthDay = user.birthDay.toLocalDateField(),
        emailAddress = user.emailAddress.toStringField(),
        phoneNumbers = PhoneNumberFilterField(
            value = userPhoneNumber.value.toStringField(),
            type = userPhoneNumber.type.toEnumField(mapper = { it.value }, parserMapper = { it.name }),
        ).toSetField()
    )
}
