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
package com.farcsal.query.api

interface NumberField<T> : LiteralField<T> where T : Number, T : Comparable<T> {
    
    fun gt(right: T): Criteria
    
    fun goe(right: T): Criteria
    
    fun loe(right: T): Criteria
    
    fun lt(right: T): Criteria

}
