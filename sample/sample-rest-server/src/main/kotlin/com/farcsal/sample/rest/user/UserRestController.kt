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
package com.farcsal.sample.rest.user

import com.farcsal.dql.query.parser.filter.field.DynamicDqlFilterFieldParser
import com.farcsal.dql.query.parser.order.field.DynamicDqlOrderFieldParser
import com.farcsal.sample.repository.api.user.model.UserFilterField
import com.farcsal.sample.repository.api.user.model.UserOrderField
import com.farcsal.sample.repository.api.util.Paging
import com.farcsal.sample.rest.util.QueryParameters
import com.farcsal.sample.service.api.user.UserService
import com.farcsal.sample.service.api.user.model.User
import com.farcsal.sample.service.api.user.model.UserCreateRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("users")
class UserRestController @Autowired constructor(
    private val userService: UserService,
    private val dynamicDqlFilterFieldParser: DynamicDqlFilterFieldParser,
    private val dynamicDqlOrderFieldParser: DynamicDqlOrderFieldParser,
) {

    @GetMapping
    fun list(
        @RequestParam(required = false, name = QueryParameters.FILTER) filterDql: String?,
        @RequestParam(required = false, name = QueryParameters.ORDER) orderDql: String?,
        @RequestParam(required = false, name = QueryParameters.OFFSET) offset: Long?,
        @RequestParam(required = false, name = QueryParameters.LIMIT) limit: Int?,
    ): List<User> {
        val filter = dynamicDqlFilterFieldParser.parseFilter<UserFilterField>(filterDql)
        val order = dynamicDqlOrderFieldParser.parseOrder<UserOrderField>(orderDql)
        return userService.list(filter, order, Paging(offset, limit))
    }

    @PostMapping
    fun create(@RequestBody request: UserCreateRequest): User {
        return userService.create(request)
    }

}
