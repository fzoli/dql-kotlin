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
package com.farcsal.sample.service.api.util.model

import com.farcsal.sample.repository.api.util.phonenumber.model.PhoneNumberFields
import com.farcsal.sample.repository.api.util.phonenumber.model.PhoneNumberType
import com.fasterxml.jackson.annotation.JsonProperty

data class PhoneNumber(
    @field:JsonProperty(PhoneNumberFields.VALUE)
    val value: String,
    @field:JsonProperty(PhoneNumberFields.TYPE)
    val type: PhoneNumberType,
)
