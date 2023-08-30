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
package com.farcsal.query.kt.filter

import com.farcsal.query.api.FloatField
import com.farcsal.query.api.NumberOperatorField

class KFloatField(value: Float?) : KNumberField<Float>(value), FloatField, NumberOperatorField<Float> {

    override fun plus(number: Float): NumberOperatorField<Float> {
        if (value == null) return this
        return KFloatField(value + number)
    }

    override fun minus(number: Float): NumberOperatorField<Float> {
        if (value == null) return this
        return KFloatField(value - number)
    }

    override fun times(number: Float): NumberOperatorField<Float> {
        if (value == null) return this
        return KFloatField(value * number)
    }

    override fun div(number: Float): NumberOperatorField<Float> {
        if (value == null) return this
        return KFloatField(value / number)
    }

}
