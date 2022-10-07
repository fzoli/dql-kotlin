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

import com.farcsal.sample.service.framework.time.TimeUnit
import com.farcsal.sample.service.framework.time.sleep.Delayer

class TestClockDelayer(private val clock: TestClock) : Delayer {
    override fun delay(unit: TimeUnit) {
        clock.setInstant(clock.instant().plusMillis(unit.toMilliseconds()))
    }
}
