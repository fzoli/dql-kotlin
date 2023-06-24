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

import com.farcsal.sample.repository.api.user.model.UserDto
import com.farcsal.sample.repository.api.user.model.asUserId
import com.farcsal.sample.repository.framework.dsl.require
import com.farcsal.sample.repository.postgresql.dsl.QUser
import com.farcsal.sample.repository.postgresql.util.mapper.toInstant
import com.querydsl.core.Tuple

@JvmName("toRequiredDto")
internal fun Tuple.toDto(user: QUser): UserDto {
    return UserDto(
        id = require(user.id).asUserId(),
        creationTime = require(user.creationTime).toInstant(),
        level = require(user.level),
        name = require(user.name),
        emailAddress = require(user.emailAddress),
    )
}

@JvmName("toOptionalDto")
internal fun Tuple?.toDto(user: QUser): UserDto? {
    if (this == null) {
        return null
    }
    return toDto(user)
}

internal fun List<Tuple>.toDto(user: QUser): List<UserDto> {
    return map { it.toDto(user) }
}
