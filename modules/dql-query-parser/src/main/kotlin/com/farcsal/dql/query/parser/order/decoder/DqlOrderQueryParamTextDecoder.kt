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
package com.farcsal.dql.query.parser.order.decoder

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

object DqlOrderQueryParamTextDecoder : DqlOrderTextDecoder {

    override fun decode(raw: String): String {
        return replaceFirstSpaceToPlus(decodeUtf8Param(raw))
    }

    private fun decodeUtf8Param(orderText: String): String {
        return URLDecoder.decode(orderText, StandardCharsets.UTF_8)
    }

    private fun replaceFirstSpaceToPlus(orderText: String): String {
        return orderText
            .replaceFirst("^\\s".toRegex(), "+")
            .replace(",\\s".toRegex(), ",+")
    }

}
