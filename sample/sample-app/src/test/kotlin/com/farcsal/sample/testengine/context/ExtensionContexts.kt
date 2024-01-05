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
package com.farcsal.sample.testengine.context

import com.farcsal.sample.testengine.extension.currentExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit.jupiter.SpringExtension

val currentTestContext: TestContext get() {
    return currentExtensionContext.testContext
}

val ExtensionContext.testContext: TestContext get() {
    return applicationContext.requireBean<TestContext>()
}

val ExtensionContext.applicationContext: ApplicationContext get() {
    return SpringExtension.getApplicationContext(this)
}
