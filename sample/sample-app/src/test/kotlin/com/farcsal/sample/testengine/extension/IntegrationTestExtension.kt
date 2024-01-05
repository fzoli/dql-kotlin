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
package com.farcsal.sample.testengine.extension

import com.farcsal.sample.testengine.context.*
import com.farcsal.sample.testengine.database.Database
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class IntegrationTestExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext) {
        contextHolder.set(context)

        context.applicationContext.getBeanProvider<Database>().forEach(Database::create)
    }

    override fun afterEach(context: ExtensionContext) {
        contextHolder.remove()

        context.applicationContext.getBeanProvider<Database>().forEach(Database::clean)
        context.applicationContext.requireBean<TestContext>().reset()
    }

    companion object {
        private val contextHolder: ThreadLocal<ExtensionContext> = ThreadLocal()
        val currentContext: ExtensionContext? get() = contextHolder.get()
    }

}
val currentExtensionContext: ExtensionContext get() {
    return requireNotNull(IntegrationTestExtension.currentContext) { "Missing extension context" }
}
