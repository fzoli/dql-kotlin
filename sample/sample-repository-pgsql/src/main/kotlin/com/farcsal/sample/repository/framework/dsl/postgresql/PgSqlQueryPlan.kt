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
package com.farcsal.sample.repository.framework.dsl.postgresql

import java.util.regex.Pattern

class PgSqlQueryPlan internal constructor(private val lines: List<String>) {

    private val text: String = java.lang.String.join("\n", lines)

    companion object {
        private val QUERY_PLAN_ROW_COUNT = Pattern.compile("rows=(\\d+)")
    }

    override fun toString(): String {
        return if (lines.isEmpty()) "QueryPlan" else "QueryPlan:\n" + lines.joinToString(separator = "\n")
    }

    val firstRowCount: Long
        get() {
            val m = QUERY_PLAN_ROW_COUNT.matcher(text)
            if (m.find()) {
                val b = m.group(1)
                return b.toLong()
            }
            throw UnsupportedOperationException("Unsupported query plan")
        }

}
