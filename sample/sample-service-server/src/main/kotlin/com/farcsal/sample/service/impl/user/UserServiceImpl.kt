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
package com.farcsal.sample.service.impl.user

import com.farcsal.query.api.filter.FilterFunction
import com.farcsal.query.api.order.OrderFunction
import com.farcsal.sample.repository.api.user.model.UserFilterField
import com.farcsal.sample.repository.api.user.model.UserOrderField
import com.farcsal.sample.service.api.user.UserService
import com.farcsal.sample.service.api.user.model.User
import com.farcsal.sample.service.api.user.model.UserCreateRequest
import com.farcsal.sample.repository.api.util.Paging
import com.farcsal.sample.service.impl.user.usecase.UserCreateUseCase
import com.farcsal.sample.service.impl.user.usecase.UserListUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
    private val listUseCase: UserListUseCase,
    private val createUseCase: UserCreateUseCase,
) : UserService {

    override fun list(
        filter: FilterFunction<UserFilterField>?,
        order: OrderFunction<UserOrderField>?,
        paging: Paging?,
    ): List<User> {
        return listUseCase.list(filter, order, paging)
    }

    override fun create(request: UserCreateRequest): User {
        return createUseCase.create(request)
    }

}
