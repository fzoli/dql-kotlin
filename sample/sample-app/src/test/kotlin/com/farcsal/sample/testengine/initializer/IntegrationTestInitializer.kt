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
package com.farcsal.sample.testengine.initializer

import com.farcsal.sample.testengine.extension.IntegrationTestExtension
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class IntegrationTestInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        initializeAppContext(applicationContext)
    }

    private fun initializeAppContext(applicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
            /** Used by [IgnoredByIntegrationTest] **/
            "integrationTestRunning=true",
            /** Disable Flyway. [IntegrationTestExtension] manages it. **/
            "spring.flyway.enabled=false"
        ).applyTo(applicationContext.environment)
    }

}
