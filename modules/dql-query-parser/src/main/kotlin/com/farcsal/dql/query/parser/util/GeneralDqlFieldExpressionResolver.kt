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

import com.farcsal.query.api.SerializedField
import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

internal inline fun <reified T: Any> Any.select(field: String): T {
    return select(T::class, field)
}

private tailrec fun <T: Any> Any.select(clazz: KClass<T>, field: String): T {
    val fieldData = splitField(field)
    val result = find(fieldData)
    return if (fieldData.remainder != null) {
        result.select(clazz, fieldData.remainder)
    } else {
        if (!clazz.isInstance(result)) {
            throw IllegalStateException("Not an instance of ${clazz}: ${fieldData.root}")
        }
        clazz.cast(result)
    }
}

private fun Any.find(fieldData: FieldData): Any {
    // Try Java fields first because iterating Java fields are 10-20 times faster
    // than iterating properties of Kotlin classes.
    val field = findJavaField(fieldData)
    if (field != null) {
        field.trySetAccessible()
        return field.get(this)
            ?: throw IllegalStateException("Null object: ${fieldData.root}")
    }
    val property = findKotlinProperty(fieldData)
        ?: throw unknownFieldException(fieldData.root)
    return property.getter.call(this)
        ?: throw IllegalStateException("Null object: ${fieldData.root}")
}

private fun Any.findJavaField(fieldData: FieldData) = this::class.java.declaredFields.find {
    val fieldInfo = it.getAnnotation(SerializedField::class.java)
    val name = fieldInfo?.name ?: it.name
    name == fieldData.root
}

private fun Any.findKotlinProperty(fieldData: FieldData) = this::class.memberProperties.find {
    val fieldInfo = it.findAnnotation<SerializedField>()
    val name = fieldInfo?.name ?: it.name
    name == fieldData.root
}

private fun splitField(field: String): FieldData {
    return if (field.contains('.')) {
        val parts = field.split('.', limit = 2)
        FieldData(root = parts[0], remainder = parts[1])
    } else {
        FieldData(root = field)
    }
}

private class FieldData(
    val root: String,
    val remainder: String? = null
)
