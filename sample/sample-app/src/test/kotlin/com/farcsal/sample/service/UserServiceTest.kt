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
package com.farcsal.sample.service

import com.farcsal.sample.repository.api.util.Paging
import com.farcsal.sample.repository.api.util.phonenumber.model.PhoneNumberType
import com.farcsal.sample.service.api.user.UserService
import com.farcsal.sample.service.api.user.model.UserCreateRequest
import com.farcsal.sample.service.api.util.model.PhoneNumber
import com.farcsal.sample.testengine.annotation.IntegrationTest
import com.farcsal.sample.testengine.context.time.second
import com.farcsal.sample.testengine.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant

@IntegrationTest
class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Test
    fun usage() = runTest {
        advanceTimeBy(1.second)
        val user = client.userService.create(UserCreateRequest(
            level = 1,
            name = "One",
            password = "password",
            emailAddress = "one@example",
            phoneNumbers = setOf(),
        ))
        client.userService.create(UserCreateRequest(
            level = 2,
            name = "Two",
            password = "password",
            emailAddress = "two@example",
            phoneNumbers = setOf(PhoneNumber("1234", PhoneNumberType.WORK)),
        ))
        val resultLocal = userService.list(
            filter = {
                id.eq(user.id) and level.goe(1) or
                name.containsIgnoreAccentCase("a") or
                phoneNumbers.oneOf.type.eq(PhoneNumberType.HOME) or
                !creationTime.after(Instant.EPOCH)
            },
            order = {
                listOf(creationTime.desc().nullsLast())
            },
            paging = Paging(offset = 0, limit = 10),
        )
        val resultRemote = client.userService.list(
            filter = {
                id.eq(user.id) and level.goe(1) or
                name.containsIgnoreAccentCase("a") or
                phoneNumbers.oneOf.type.eq(PhoneNumberType.HOME) or
                !creationTime.after(Instant.EPOCH)
            },
            order = {
                listOf(creationTime.desc().nullsLast())
            },
            paging = Paging(offset = 0, limit = 10),
        )
        Assertions.assertEquals(1, resultLocal.size)
        Assertions.assertEquals(1, resultRemote.size)
        Assertions.assertEquals(user, resultLocal.first())
        Assertions.assertEquals(user, resultRemote.first())
    }

}
