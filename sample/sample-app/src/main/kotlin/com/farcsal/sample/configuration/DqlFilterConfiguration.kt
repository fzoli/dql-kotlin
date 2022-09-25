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

import com.farcsal.dql.query.parser.filter.DqlFilterFactory
import com.farcsal.dql.query.parser.filter.field.DynamicDqlFilterFieldParser
import com.farcsal.dql.query.parser.filter.invoker.extend
import com.farcsal.dql.query.parser.filter.invoker.strategyFactories
import com.farcsal.dql.query.parser.filter.invoker.withAnyStrategy
import com.farcsal.sample.dql.query.parser.filter.invoker.strategy.DqlMethodInvokerUnaccentStringStrategyFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DqlFilterConfiguration {

    @Bean
    fun dqlFilterFactory(): DqlFilterFactory {
        return DqlFilterFactory(
            methodInvokerStrategyFactories = strategyFactories
                .extend(listOf(
                    DqlMethodInvokerUnaccentStringStrategyFactory(),
                ))
                .withAnyStrategy()
        )
    }

    @Bean
    fun dynamicDqlFilterFieldParser(dqlFilterFactory: DqlFilterFactory): DynamicDqlFilterFieldParser {
        return DynamicDqlFilterFieldParser(dqlFilterFactory)
    }

}
