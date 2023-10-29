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
package com.farcsal.sample.service.client

import com.farcsal.sample.service.api.user.UserService
import com.farcsal.sample.service.api.user.serializer.json
import com.farcsal.sample.service.client.user.UserApi
import com.farcsal.sample.service.client.user.UserServiceClient
import com.farcsal.sample.service.client.util.LocaleInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class ClientServices(
    baseUrl: String,
    locale: Locale = Locale.getDefault(),
) {

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(1L, TimeUnit.MINUTES)
        .connectTimeout(1L, TimeUnit.MINUTES)
        .writeTimeout(1L, TimeUnit.MINUTES)
        .addInterceptor(LocaleInterceptor(locale))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val userService: UserService = UserServiceClient(retrofit.create(UserApi::class.java))

}
