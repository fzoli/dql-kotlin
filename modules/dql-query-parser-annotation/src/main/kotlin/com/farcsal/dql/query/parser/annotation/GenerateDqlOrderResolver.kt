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
package com.farcsal.dql.query.parser.annotation

/**
 * Triggers KSP generation of a [com.farcsal.dql.query.parser.order.resolver.DqlOrderFieldExpressionResolver]
 * and a [com.farcsal.dql.query.parser.order.field.StaticDqlOrderFieldParser] for the annotated class.
 *
 * Each constructor property must either:
 * - implement [com.farcsal.query.api.OrderField] (leaf node), or
 * - be another class annotated with [GenerateDqlOrderResolver] (nested resolver).
 *
 * Use [com.farcsal.query.api.SerializedField] on properties to define DQL field names.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenerateDqlOrderResolver
