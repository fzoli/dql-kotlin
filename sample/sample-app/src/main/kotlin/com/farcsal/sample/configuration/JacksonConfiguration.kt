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

import com.farcsal.sample.rest.util.jackson.time.IsoTimeModule
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class JacksonConfiguration {

    @Bean
    fun objectMapperBuilder(): Jackson2ObjectMapperBuilder {
        val builder = Jackson2ObjectMapperBuilder()
        builder.serializationInclusion(JsonInclude.Include.NON_NULL)
        builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        builder.indentOutput(false)
        builder.modulesToInstall(
            Jdk8Module(),
            JavaTimeModule(),
            IsoTimeModule(),  // IsoTime is after JavaTime to overload some adapter.
            KotlinModule.Builder()
                .enable(KotlinFeature.StrictNullChecks)
                .build()
        )
        return builder
    }

    @Bean
    @Primary // Primary annotation is used in spring boot code so we use a qualifier too.
    @Qualifier("app")
    fun objectMapper(objectMapperBuilder: Jackson2ObjectMapperBuilder): ObjectMapper {
        return objectMapperBuilder
            .createXmlMapper(false)
            .build()
    }

}
