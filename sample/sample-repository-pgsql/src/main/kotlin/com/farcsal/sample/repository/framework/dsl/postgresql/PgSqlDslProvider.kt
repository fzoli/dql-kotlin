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
package com.farcsal.sample.repository.framework.dsl.postgresql

import com.farcsal.sample.repository.framework.sql.SqlConnectionResolver
import com.querydsl.sql.PostgreSQLTemplates
import com.querydsl.sql.RelationalPath
import com.querydsl.sql.dml.SQLDeleteClause
import com.querydsl.sql.dml.SQLInsertClause
import com.querydsl.sql.dml.SQLUpdateClause

class PgSqlDslProvider(
    private val connectionResolver: SqlConnectionResolver,
    private val templates: PostgreSQLTemplates,
) {

    constructor(connectionResolver: SqlConnectionResolver) : this(connectionResolver, PgSqlTemplates())

    fun createSqlQueryFactory(): PgSqlQueryFactory {
        return PgSqlQueryFactory(connectionResolver.getCurrentTransactionConnection(), templates)
    }

    fun createSqlInsertClause(entity: RelationalPath<*>): SQLInsertClause {
        return SQLInsertClause(connectionResolver.getCurrentTransactionConnection(), templates, entity)
    }

    fun createSqlUpdateClause(entity: RelationalPath<*>): SQLUpdateClause {
        return SQLUpdateClause(connectionResolver.getCurrentTransactionConnection(), templates, entity)
    }

    fun createSqlDeleteClause(entity: RelationalPath<*>): SQLDeleteClause {
        return SQLDeleteClause(connectionResolver.getCurrentTransactionConnection(), templates, entity)
    }

}
