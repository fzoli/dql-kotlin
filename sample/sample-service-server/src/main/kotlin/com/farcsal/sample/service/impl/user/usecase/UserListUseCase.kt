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

import com.farcsal.query.api.filter.FilterFunction
import com.farcsal.query.api.order.OrderFunction
import com.farcsal.sample.repository.api.user.UserRepository
import com.farcsal.sample.repository.api.user.model.UserFilterField
import com.farcsal.sample.repository.api.user.model.UserOrderField
import com.farcsal.sample.service.api.user.model.User
import com.farcsal.sample.repository.api.util.Paging
import com.farcsal.sample.service.impl.util.framework.annotation.UseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@UseCase
open class UserListUseCase @Autowired constructor(
    private val userRepository: UserRepository,
) {

    @Transactional
    open fun list(
        filter: FilterFunction<UserFilterField>?,
        order: OrderFunction<UserOrderField>?,
        paging: Paging?,
    ): List<User> {
        val users = userRepository.query(filter, order, paging)
        val phoneNumbers = userRepository.queryPhoneNumbers(users.map { it.id }.toSet())
        return toPublic(users, phoneNumbers)
    }

}
