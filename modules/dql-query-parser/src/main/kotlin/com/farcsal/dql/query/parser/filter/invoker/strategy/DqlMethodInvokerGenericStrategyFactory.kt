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
package com.farcsal.dql.query.parser.filter.invoker.strategy

import com.farcsal.query.api.Field

abstract class DqlMethodInvokerGenericStrategyFactory<T: Field> : DqlMethodInvokerStrategyFactory {

    final override fun create(expr: Field): DqlMethodInvokerStrategy? {
        val field = cast(expr)
        if (field != null) {
            return createStrategy(field)
        }
        return null
    }

    abstract fun cast(expr: Field) : T?
    abstract fun createStrategy(expr: T): DqlMethodInvokerStrategy

}
