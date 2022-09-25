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
package com.farcsal.sample.service.api.user

import com.farcsal.query.api.filter.FilterFunction
import com.farcsal.query.api.order.OrderFunction
import com.farcsal.sample.repository.api.user.model.UserFilterField
import com.farcsal.sample.repository.api.user.model.UserOrderField
import com.farcsal.sample.service.api.user.model.User
import com.farcsal.sample.service.api.user.model.UserCreateRequest
import com.farcsal.sample.repository.api.util.Paging

interface UserService {
    fun list(
        filter: FilterFunction<UserFilterField>? = null,
        order: OrderFunction<UserOrderField>? = null,
        paging: Paging? = null,
    ): List<User>

    fun create(request: UserCreateRequest): User

}
