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

import com.farcsal.datatype.toEnum
import com.farcsal.sample.repository.api.user.model.UserPhoneNumberDto
import com.farcsal.sample.repository.api.user.model.asUserId
import com.farcsal.sample.repository.api.util.phonenumber.model.PhoneNumberType
import com.farcsal.sample.repository.framework.dsl.require
import com.farcsal.sample.repository.postgresql.dsl.QUserPhoneNumber
import com.querydsl.core.Tuple

@JvmName("toRequiredDto")
internal fun Tuple.toDto(phoneNumber: QUserPhoneNumber): UserPhoneNumberDto {
    return UserPhoneNumberDto(
        userId = require(phoneNumber.userId).asUserId(),
        value = require(phoneNumber.value),
        type = require(phoneNumber.type).toEnum<PhoneNumberType> { it.value },
    )
}

@JvmName("toOptionalDto")
internal fun Tuple?.toDto(phoneNumber: QUserPhoneNumber): UserPhoneNumberDto? {
    if (this == null) {
        return null
    }
    return toDto(phoneNumber)
}

internal fun List<Tuple>.toDto(phoneNumber: QUserPhoneNumber): List<UserPhoneNumberDto> {
    return map { it.toDto(phoneNumber) }
}
