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
package com.farcsal.sample.configuration

import com.farcsal.sample.ApplicationEnvironment
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration(private val applicationEnvironment: ApplicationEnvironment) {

    @Bean
    @Primary
    fun getDataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.postgresql.Driver")
        dataSource.url = applicationEnvironment.databaseJdbcUrl
        dataSource.username = applicationEnvironment.databaseUsername
        dataSource.password = applicationEnvironment.databasePassword
        if (applicationEnvironment.databaseConnectionPoolEnabled) {
            val pool = HikariDataSource()
            pool.dataSource = dataSource
            return pool
        }
        return dataSource
    }

    @Component
    class DataSourceCleaner @Autowired constructor(private val dataSource: DataSource) : DisposableBean {
        override fun destroy() {
            if (dataSource is HikariDataSource) {
                dataSource.close()
            }
        }
    }

}
