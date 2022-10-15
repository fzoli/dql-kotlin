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

import com.farcsal.dql.query.parser.order.DqlOrderFactory
import com.farcsal.dql.query.parser.order.OrderFunctionFactory
import com.farcsal.dql.query.parser.order.field.DynamicDqlOrderFieldParser
import com.farcsal.dql.query.parser.order.field.decorator.LocalizedOrderFieldDecorator
import com.farcsal.dql.query.parser.order.field.decorator.OrderFieldDecorator
import com.farcsal.dql.query.parser.util.locale.DqlLocaleProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DqlOrderConfiguration {

    @Bean
    fun dqlOrderFactory(): DqlOrderFactory {
        return DqlOrderFactory()
    }

    @Bean
    fun orderFieldDecorator(localeProvider: DqlLocaleProvider): OrderFieldDecorator {
        return LocalizedOrderFieldDecorator(localeProvider)
    }

    @Bean
    fun orderFunctionFactory(
        dqlOrderFactory: DqlOrderFactory,
        orderFieldDecorator: OrderFieldDecorator
    ): OrderFunctionFactory {
        return OrderFunctionFactory(dqlOrderFactory, orderFieldDecorator)
    }

    @Bean
    fun dynamicDqlOrderFieldParser(orderFunctionFactory: OrderFunctionFactory): DynamicDqlOrderFieldParser {
        return DynamicDqlOrderFieldParser(orderFunctionFactory)
    }

}
