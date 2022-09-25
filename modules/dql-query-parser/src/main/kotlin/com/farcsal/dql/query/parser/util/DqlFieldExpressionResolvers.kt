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
package com.farcsal.dql.query.parser.util

fun isEmbeddedField(
    fieldDefinition: String,
    fieldName: String
): Boolean {
    return fieldDefinition.startsWith("$fieldName.")
}

fun asEmbeddedField(
    fieldDefinition: String,
    fieldName: String
): String {
    if (!isEmbeddedField(fieldDefinition, fieldName)) {
        throw IllegalArgumentException("Not an embedded field")
    }
    return fieldDefinition.substring("$fieldName.".length)
}

fun unknownFieldException(fieldDefinition: String): RuntimeException {
    throw UnsupportedOperationException("Field has not found: $fieldDefinition")
}
