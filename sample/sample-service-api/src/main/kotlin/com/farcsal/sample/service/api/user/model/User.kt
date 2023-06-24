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
package com.farcsal.sample.service.api.user.model

import com.farcsal.sample.repository.api.user.model.UserFields
import com.farcsal.sample.repository.api.user.model.UserId
import com.farcsal.sample.service.api.util.model.PhoneNumber
import com.farcsal.sample.service.api.util.serializer.InstantSerializer
import com.farcsal.sample.service.api.util.serializer.ids.UserIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class User(
    @SerialName(UserFields.ID)
    @Serializable(with = UserIdSerializer::class)
    val id: UserId,
    @SerialName(UserFields.CREATION_TIME)
    @Serializable(with = InstantSerializer::class)
    val creationTime: Instant,
    @SerialName(UserFields.LEVEL)
    val level: Int,
    @SerialName(UserFields.NAME)
    val name: String,
    @SerialName(UserFields.EMAIL_ADDRESS)
    val emailAddress: String,
    @SerialName(UserFields.PHONE_NUMBERS)
    val phoneNumbers: Set<PhoneNumber>,
)
