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
package com.farcsal.sample.testengine.clock

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class TestClock(private val zoneId: ZoneId) : Clock() {

    private var instant: Instant = Instant.EPOCH

    override fun getZone(): ZoneId {
        return zoneId
    }

    override fun withZone(zoneId: ZoneId): Clock {
        return TestClock(zoneId)
    }

    override fun instant(): Instant {
        return instant
    }

    fun setInstant(instant: Instant) {
        this.instant = instant
    }

}
