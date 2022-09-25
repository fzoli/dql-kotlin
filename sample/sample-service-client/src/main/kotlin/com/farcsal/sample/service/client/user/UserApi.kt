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
package com.farcsal.sample.service.client.user

import com.farcsal.sample.rest.util.QueryParameters
import com.farcsal.sample.service.api.user.model.User
import com.farcsal.sample.service.api.user.model.UserCreateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

internal interface UserApi {

    @GET("users")
    suspend fun list(
        @Query(QueryParameters.FILTER) filter: String?,
        @Query(QueryParameters.ORDER) order: String?,
        @Query(QueryParameters.OFFSET) offset: Long?,
        @Query(QueryParameters.LIMIT) limit: Int?,
    ): List<User>

    @POST("users")
    suspend fun create(@Body request: UserCreateRequest): User

}
