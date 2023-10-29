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
package com.farcsal.sample.service.impl.user.usecase

import com.farcsal.sample.repository.api.user.UserRepository
import com.farcsal.sample.repository.api.user.model.UserCreateDto
import com.farcsal.sample.repository.api.user.model.UserPhoneNumberDto
import com.farcsal.sample.service.api.user.model.User
import com.farcsal.sample.service.api.user.model.UserCreateRequest
import com.farcsal.sample.service.framework.annotation.UseCase
import com.farcsal.sample.service.impl.util.generatePasswordHash
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.Instant

@UseCase
open class UserCreateUseCase @Autowired constructor(
    private val clock: Clock,
    private val userRepository: UserRepository,
) {

    @Transactional
    open fun create(
        request: UserCreateRequest
    ): User {
        val id = userRepository.create(UserCreateDto(
            level = request.level,
            name = request.name,
            passwordHash = request.password.generatePasswordHash().toString(),
            birthDay = request.birthDay,
            emailAddress = request.emailAddress,
            creationTime = Instant.now(clock),
        ))
        request.phoneNumbers.forEach {
            userRepository.addPhoneNumber(UserPhoneNumberDto(
                userId = id,
                value = it.value,
                type = it.type
            ))
        }
        val user = requireNotNull(userRepository.findById(id))
        val phoneNumbers = userRepository.queryPhoneNumbers(setOf(id))
        return toPublic(listOf(user), phoneNumbers).first()
    }

}
