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
package com.farcsal.sample.testengine.database

import org.flywaydb.core.Flyway
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ContainerDatabase private constructor(private val container: JdbcDatabaseContainer<*>) : Database {

    private val lock = ReentrantLock()

    constructor() : this(createContainer())

    companion object {
        private fun createContainer(): JdbcDatabaseContainer<*> {
            val postgres = DockerImageName
                .parse("dql-sample/postgres:latest")
                .asCompatibleSubstituteFor("postgres")
            return PostgreSQLContainer<Nothing>(postgres).apply {
                withDatabaseName("app")
                withUsername("postgres")
                withPassword("dev")
                withStartupTimeout(Duration.ofSeconds(600))
            }
        }
    }

    override val jdbcUrl: String
        get() = container.jdbcUrl

    override val username: String
        get() = container.username

    override val password: String
        get() = container.password

    override val running: Boolean
        get() = container.isRunning

    override fun start(): Database {
        lock.withLock {
            expectNotRunning()
            container.start()
            return this
        }
    }

    override fun stop() {
        lock.withLock {
            expectRunning()
            container.stop()
        }
    }

    override fun clean() {
        lock.withLock {
            expectRunning()
            createFlyway().apply {
                clean()
            }
        }
    }

    override fun create() {
        lock.withLock {
            expectRunning()
            createFlyway().apply {
                migrate()
            }
        }
    }

    private fun expectRunning() {
        check(container.isRunning) { "Not running" }
    }

    private fun expectNotRunning() {
        check(!container.isRunning) { "Already running" }
    }

    private fun createFlyway(): Flyway {
        return Flyway
            .configure()
            .dataSource(container.jdbcUrl, container.username, container.password)
            .load()
    }

}
