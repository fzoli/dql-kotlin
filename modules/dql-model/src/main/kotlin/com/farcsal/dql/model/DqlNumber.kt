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
package com.farcsal.dql.model

import java.math.BigDecimal
import java.math.BigInteger

@JvmInline
value class DqlNumber(private val bigDecimal: BigDecimal) {

    constructor(int: Int): this(int.toBigDecimal())

    constructor(long: Long): this(long.toBigDecimal())

    constructor(bigInt: BigInteger): this(bigInt.toBigDecimal())

    constructor(double: Double): this(double.toBigDecimal())

    constructor(float: Float): this(float.toBigDecimal())

    fun toInt(): Int {
        return bigDecimal.intValueExact()
    }

    fun toLong(): Long {
        return bigDecimal.longValueExact()
    }

    fun toBigInt(): BigInteger {
        return bigDecimal.toBigIntegerExact()
    }

    fun toDouble(): Double {
        return bigDecimal.toDouble()
    }

    fun toFloat(): Float {
        return bigDecimal.toFloat()
    }

    fun toBigDecimal(): BigDecimal {
        return bigDecimal
    }

}
