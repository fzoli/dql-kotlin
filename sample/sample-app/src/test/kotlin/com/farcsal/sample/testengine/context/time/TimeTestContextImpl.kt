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
package com.farcsal.sample.testengine.context.time

import com.farcsal.sample.service.framework.time.Day
import com.farcsal.sample.service.framework.time.Millisecond
import com.farcsal.sample.service.framework.time.Second
import com.farcsal.sample.testengine.clock.TestClock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

@Component
class TimeTestContextImpl @Autowired constructor(
    private val clock: Clock,
) : TimeTestContext {

    override val timeZone: ZoneId
        get() = clock.zone

    override var now: Instant
        get() = clock.instant()
        set(value) = (clock as TestClock).setInstant(value)

    fun reset() {
        now = Instant.EPOCH
    }

    override fun advanceTimeBy(ms: Millisecond) {
        advanceTimeBy(ms = ms.value)
    }

    override fun advanceTimeBy(second: Second) {
        advanceTimeBy(second = second.value)
    }

    override fun advanceTimeBy(day: Day) {
        advanceTimeBy(day = day.value)
    }

    override fun advanceTimeBy(ms: Long, second: Long, day: Long) {
        now = now.atZone(timeZone)
            .plusDays(day)
            .plusSeconds(second)
            .toInstant()
            .plusMillis(ms)
    }

}
