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

import com.querydsl.core.Tuple
import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.Expression
import com.querydsl.sql.PostgreSQLTemplates
import java.sql.Connection

class PgSqlQueryFactory(private val connection: Connection, private val templates: PostgreSQLTemplates) {

    /**
     * Creates a new query instance with the given projection
     *
     * @param expr projection
     * @param <T>
     * @return select(expr)
    </T> */
    fun <T> select(expr: Expression<T>): PgSqlQuery<T> {
        return PgSqlQuery<T>(connection, templates)
            .select(expr = expr)
    }

    /**
     * Creates a new query instance with the given projection
     *
     * @param exprs projection
     * @return select(exprs)
     */
    fun select(vararg exprs: Expression<*>): PgSqlQuery<Tuple> {
        return PgSqlQuery<Tuple>(connection, templates)
            .select(exprs = exprs)
    }

    /**
     * Creates a new query instance with the given source and projection
     *
     * @param from projection and source
     * @param <T>
     * @return select(from).from(from)
     */
    fun <T> selectFrom(from: EntityPath<T>): PgSqlQuery<T> {
        return PgSqlQuery<T>(connection, templates)
            .select(from)
            .from(from)
    }

}
