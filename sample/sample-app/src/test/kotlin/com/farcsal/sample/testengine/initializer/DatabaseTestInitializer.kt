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

import com.farcsal.sample.testengine.database.ContainerDatabase
import com.farcsal.sample.testengine.database.Database
import com.farcsal.sample.testengine.context.registerSingleton
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment

class DatabaseTestInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val env: ConfigurableEnvironment = applicationContext.environment

        val database = createDatabase(env)

        database.start()
        Runtime.getRuntime().addShutdownHook(ShutdownHook(database))

        applicationContext.registerSingleton(database)

        initializeAppContext(applicationContext, database)
    }

    private fun createDatabase(env: ConfigurableEnvironment?): Database {
        requireNotNull(env) { "Environment is required" }
        return ContainerDatabase()
    }

    private fun initializeAppContext(
        applicationContext: ConfigurableApplicationContext,
        database: Database
    ) {
        TestPropertyValues.of(
            /** Prevent PostgreSQL ERROR: cached plan must not change result type **/
            "database.connectionPool.enabled=false",
            /** Database configuration parameters **/
            "database.jdbcUrl=" + database.jdbcUrl,
            "database.username=" + database.username,
            "database.password=" + database.password
        ).applyTo(applicationContext.environment)
    }

    private class ShutdownHook(private val database: Database) : Thread() {
        override fun run() {
            database.stop()
        }
    }

}
