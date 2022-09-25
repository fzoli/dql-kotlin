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
package com.farcsal.sample.service.impl.util

import com.toxicbakery.bcrypt.Bcrypt

typealias Password = String

@JvmInline
value class PasswordHash(private val hash: String) {

    companion object {
        fun generate(password: Password, saltRounds: Int = 12): PasswordHash {
            val bytes = Bcrypt.hash(password, saltRounds)
            return PasswordHash(String(bytes))
        }
    }

    override fun toString(): String {
        return hash
    }

    fun matches(password: Password): Boolean {
        val bytes = hash.toByteArray()
        return Bcrypt.verify(password, bytes)
    }

}

fun Password.generatePasswordHash(): PasswordHash {
    return PasswordHash.generate(this)
}

fun String.toPasswordHash(): PasswordHash {
    return PasswordHash(this)
}
