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
package com.farcsal.sample.repository.postgresql.user

import com.farcsal.query.api.filter.FilterFunction
import com.farcsal.query.api.filter.evaluate
import com.farcsal.query.api.order.OrderFunction
import com.farcsal.query.api.order.evaluate
import com.farcsal.query.querydsl.extension.query.orderBy
import com.farcsal.query.querydsl.extension.query.where
import com.farcsal.sample.repository.api.user.UserRepository
import com.farcsal.sample.repository.api.user.model.UserCreateDto
import com.farcsal.sample.repository.api.user.model.UserDto
import com.farcsal.sample.repository.api.user.model.UserFilterField
import com.farcsal.sample.repository.api.user.model.UserOrderField
import com.farcsal.sample.repository.api.user.model.UserPhoneNumberDto
import com.farcsal.sample.repository.api.util.Paging
import com.farcsal.sample.repository.framework.dsl.postgresql.PgSqlDslProvider
import com.farcsal.sample.repository.postgresql.dsl.QUser.user
import com.farcsal.sample.repository.postgresql.dsl.QUserPhoneNumber.userPhoneNumber
import com.farcsal.sample.repository.postgresql.user.model.createFilterField
import com.farcsal.sample.repository.postgresql.user.model.createOrderField
import com.farcsal.sample.repository.postgresql.user.model.toDto
import com.farcsal.sample.repository.postgresql.util.dml.executeWithRequiredKey
import com.farcsal.sample.repository.postgresql.util.mapper.toLocalDateTime
import com.farcsal.sample.repository.postgresql.util.paging.pageBy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class UserPgSqlRepository @Autowired constructor(
    private val dslProvider: PgSqlDslProvider,
) : UserRepository {

    override fun query(
        filter: FilterFunction<UserFilterField>?,
        order: OrderFunction<UserOrderField>?,
        paging: Paging?,
    ): List<UserDto> {
        return dslProvider.createSqlQueryFactory()
            .select(
                user.id,
                user.level,
                user.name,
                user.creationTime,
                user.emailAddress,
            )
            .from(user)
            .where(user.id.eqAny(dslProvider.createSqlQueryFactory()
                .select(user.id)
                .from(user)
                .leftJoin(userPhoneNumber).on(userPhoneNumber.userId.eq(user.id))
                .where(filter.evaluate(createFilterField(user, userPhoneNumber)))
                .groupBy(user.id)
            ))
            .orderBy(order.evaluate(createOrderField(user)))
            .pageBy(paging)
            .fetch()
            .toDto(user)
    }

    override fun queryPhoneNumbers(
        userIds: Set<UUID>
    ): Set<UserPhoneNumberDto> {
        return dslProvider.createSqlQueryFactory()
            .select(
                userPhoneNumber.userId,
                userPhoneNumber.value,
                userPhoneNumber.type,
            )
            .from(userPhoneNumber)
            .where(userPhoneNumber.userId.`in`(userIds))
            .fetch()
            .toDto(userPhoneNumber)
            .toSet()
    }

    override fun findById(id: UUID): UserDto? {
        return dslProvider.createSqlQueryFactory()
            .select(
                user.id,
                user.level,
                user.name,
                user.creationTime,
                user.emailAddress,
            )
            .from(user)
            .where(user.id.eq(id))
            .fetchOne()
            .toDto(user)
    }

    override fun create(dto: UserCreateDto): UUID {
        return dslProvider.createSqlInsertClause(user)
            .set(user.level, dto.level)
            .set(user.name, dto.name)
            .set(user.passwordHash, dto.passwordHash)
            .set(user.emailAddress, dto.emailAddress)
            .set(user.creationTime, dto.creationTime.toLocalDateTime())
            .executeWithRequiredKey(user.id)
    }

    override fun addPhoneNumber(dto: UserPhoneNumberDto) {
        dslProvider.createSqlInsertClause(userPhoneNumber)
            .set(userPhoneNumber.userId, dto.userId)
            .set(userPhoneNumber.value, dto.value)
            .set(userPhoneNumber.type, dto.type.name)
            .execute()
    }

}
