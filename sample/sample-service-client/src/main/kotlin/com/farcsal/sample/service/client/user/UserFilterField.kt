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

import com.farcsal.query.api.SetFieldMethods
import com.farcsal.query.dql.*
import com.farcsal.sample.query.dql.DqlUnaccentStringField
import com.farcsal.sample.repository.api.user.model.UserFields
import com.farcsal.sample.repository.api.user.model.UserFilterField
import com.farcsal.sample.repository.api.util.ids.asUserId
import com.farcsal.sample.service.client.util.model.phoneNumberFilterField

internal fun userFilterField(prefix: String = "") = UserFilterField(
    id = DqlUuidField(prefix + UserFields.ID).asUserId(),
    creationTime = DqlInstantField(prefix + UserFields.CREATION_TIME),
    level = DqlIntField(prefix + UserFields.LEVEL),
    name = DqlUnaccentStringField(DqlStringField(prefix + UserFields.NAME)),
    emailAddress = DqlStringField(prefix + UserFields.EMAIL_ADDRESS),
    phoneNumbers = DqlSetField(phoneNumberFilterField(prefix + UserFields.PHONE_NUMBERS + "." + SetFieldMethods.ONE_OF + ".")),
)
