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

@file:Suppress("unused")

package com.maproductions.mohamedalaa.core

import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.maproductions.mohamedalaa.core.internal.MATypes
import java.lang.reflect.Field
import java.lang.reflect.Type

internal fun Any.getClassDeclaredFieldsAndSuperclassesDeclaredFields(): List<Field> {
    return javaClass.declaredFieldsForSuperclassesOnly(javaClass.declaredFields.filterNotNull())
}

internal fun Any?.toJsonWithFullTypeInfo(genericType: Type, gson: Gson? = null): String {
    val usedGson = gson ?: privateGeneratedGson

    val value = if (this == null) null else object : GsonConverterWithFullTypeInfo(genericType, usedGson){}.toJson(this)

    return if (value != null && value != "null") {
        value
    }else {
        throw RuntimeException("Can't convert $this to JSON String")
    }
}

internal fun Any?.toJsonOrNullWithFullTypeInfo(genericType: Type, gson: Gson? = null): String? = runCatching {
    toJsonWithFullTypeInfo(genericType, gson)
}.getOrNull()

internal fun String?.fromJsonWithFullTypeInfo(genericType: Type?, gson: Gson? = null): Any {
    val usedGson = gson ?: privateGeneratedGson

    val value = if (this == null || genericType == null) null else {
        object : GsonConverterWithFullTypeInfo(genericType, usedGson){}.fromJson(this) as? Any
    }

    return value ?: throw RuntimeException("Can't convert $this to a non-null object")
}

internal fun String?.fromJsonOrNullWithFullTypeInfo(genericType: Type?, gson: Gson? = null): Any? = runCatching {
    fromJsonWithFullTypeInfo(genericType, gson)
}.getOrNull()

private abstract class GsonConverterWithFullTypeInfo(
    private val genericType: Type,
    private val gson: Gson? = null
) {

    fun <E> toJson(element: E?): String? {
        val type = MATypes.canonicalizeOrNullAndEliminateWildcardTypes(genericType) ?: `$Gson$Types`.canonicalize(genericType)

        val usedGson = gson ?: privateGeneratedGson

        return if (element == null) null else usedGson.toJson(element, type)
    }

    fun <E> fromJson(json: String?): E? {
        val type = MATypes.canonicalizeOrNullAndEliminateWildcardTypes(genericType) ?: `$Gson$Types`.canonicalize(genericType)

        if (json != null) {
            if (`$MA$Gson`.checkObjectDeclarationEvenIfNotAnnotated) {
                (type as? Class<*>)?.objectInstance()?.apply {
                    @Suppress("UNCHECKED_CAST")
                    (this as? E)?.also { return it }
                }
            }
        }

        val usedGson = gson ?: privateGeneratedGson

        return if (json == null) null else usedGson.fromJson(json, type)
    }

}