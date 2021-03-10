/*
 * Copyright © 2020 Mohamed Alaa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.maproductions.mohamedalaa.core

import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.maproductions.mohamedalaa.core.internal.canonicalizeOrNull
import com.maproductions.mohamedalaa.core.java.GsonConverter
import java.lang.reflect.Field
import java.lang.reflect.Type

internal fun Any.getClassDeclaredFieldsAndSuperclassesDeclaredFields(): List<Field> {
    return javaClass.declaredFieldsForSuperclassesOnly(javaClass.declaredFields.filterNotNull())
}

internal fun Any?.toJsonOrNullWithFullTypeInfo(genericType: Type, gson: Gson? = null): String? = this?.run {
    val usedGson = gson?.addTypeAdapters() ?: privateGeneratedGson

    try { object : GsonConverterWithFullTypeInfo(genericType, usedGson){}.toJsonOrNull(this) } catch (e: Exception) { null }
}

internal fun String?.fromJsonOrNullWithFullTypeInfo(genericType: Type?, gson: Gson? = null): Any? = this?.run {
    if (genericType == null) {
        return@run null
    }

    val usedGson = gson?.addTypeAdapters() ?: privateGeneratedGson

    try { object : GsonConverterWithFullTypeInfo(genericType, usedGson){}.fromJsonOrNull(this) as Any? } catch (e: Exception) { null }
}

internal abstract class GsonConverterWithFullTypeInfo(
    private val genericType: Type,
    private val gson: Gson? = null
) {

    fun <E> toJsonOrNull(element: E?): String? {
        val type = GsonConverter.canonicalizeOrNull(genericType) ?: `$Gson$Types`.canonicalize(genericType)

        val usedGson = gson?.addTypeAdapters() ?: privateGeneratedGson

        return try { usedGson.toJson(element, type) } catch (e: Exception) { null }
    }

    fun <E> fromJsonOrNull(json: String?): E? {
        val type = GsonConverter.canonicalizeOrNull(genericType) ?: `$Gson$Types`.canonicalize(genericType)

        (type as? Class<*>)?.kotlin?.objectInstance?.apply {
            @Suppress("UNCHECKED_CAST")
            return this as E?
        }

        val usedGson = gson?.addTypeAdapters() ?: privateGeneratedGson

        return try { usedGson.fromJson(json, type) } catch (e: Exception) { null }
    }

}
