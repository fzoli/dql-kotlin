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
package com.farcsal.sample.repository.framework.sql

import com.farcsal.sample.repository.framework.transaction.TransactionValidator
import org.springframework.jdbc.datasource.DataSourceUtils
import java.sql.Connection
import javax.sql.DataSource

class SqlConnectionResolver constructor(
    private val dataSource: DataSource,
    private val transactionValidator: TransactionValidator,
) {

    fun getCurrentTransactionConnection(): Connection {
        // DataSourceUtils#getConnection creates a new connection if there are no open connections,
        // so first we validate the transaction to prevent inconsistent state.
        // Anyway it is not a good idea to manipulate data or read it without transaction.
        transactionValidator.requireTransaction()
        return DataSourceUtils.getConnection(dataSource)
    }

}
