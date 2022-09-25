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

import com.querydsl.core.DefaultQueryMetadata
import com.querydsl.core.QueryFlag
import com.querydsl.core.QueryMetadata
import com.querydsl.core.Tuple
import com.querydsl.core.types.Expression
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.sql.Configuration
import com.querydsl.sql.PostgreSQLTemplates
import com.querydsl.sql.SQLTemplates
import com.querydsl.sql.postgresql.AbstractPostgreSQLQuery
import java.sql.Connection
import java.sql.SQLException
import java.util.*

class PgSqlQuery<T> : AbstractPostgreSQLQuery<T, PgSqlQuery<T>> {

    constructor(conn: Connection, templates: SQLTemplates) : this(
        conn,
        Configuration(templates),
        DefaultQueryMetadata()
    )

    @JvmOverloads
    constructor(
        conn: Connection,
        configuration: Configuration = Configuration(PostgreSQLTemplates.DEFAULT),
        metadata: QueryMetadata = DefaultQueryMetadata()
    ) : super(conn, configuration, metadata)

    override fun clone(conn: Connection): PgSqlQuery<T> {
        val q = PgSqlQuery<T>(conn, getConfiguration(), metadata.clone())
        q.clone(this)
        return q
    }

    override fun <U> select(expr: Expression<U>): PgSqlQuery<U> {
        queryMixin.setProjection(expr)
        return castReturnType()
    }

    override fun select(vararg exprs: Expression<*>): PgSqlQuery<Tuple> {
        queryMixin.setProjection(*exprs)
        return castReturnType()
    }

    fun forUpdate(path: Expression<*>): PgSqlQuery<T> {
        val forUpdateOp = ExpressionUtils.template(Any::class.java, "\nfor update of {0}", path)
        val forUpdateFlag = QueryFlag(QueryFlag.Position.END, forUpdateOp)
        return addFlag(forUpdateFlag)
    }

    fun forShare(path: Expression<*>): PgSqlQuery<T> {
        val forShareOp = ExpressionUtils.template(Any::class.java, "\nfor share of {0}", path)
        val forUpdateFlag = QueryFlag(QueryFlag.Position.END, forShareOp)
        return addFlag(forUpdateFlag)
    }

    fun explain(): PgSqlQueryPlan {
        val q = clone()
        q.addFlag(QueryFlag(QueryFlag.Position.START, "explain "))
        try {
            q.results.use { rs ->
                val lines: MutableList<String> = ArrayList()
                while (rs.next()) {
                    lines.add(rs.getString(1))
                }
                return PgSqlQueryPlan(Collections.unmodifiableList(lines))
            }
        } catch (ex: SQLException) {
            throw configuration.translate(ex)
        }
    }

    override fun fetchOne(): T? {
        return super.fetchOne()
    }

    fun requireOne(): T {
        return requireNotNull(fetchOne())
    }

    private fun <U> castReturnType(): PgSqlQuery<U> {
        @Suppress("UNCHECKED_CAST")
        return this as PgSqlQuery<U>
    }

}
