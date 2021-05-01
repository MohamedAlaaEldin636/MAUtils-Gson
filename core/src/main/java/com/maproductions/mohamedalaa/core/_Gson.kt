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

import android.net.Uri
import com.google.gson.*
import com.maproductions.mohamedalaa.core.internal.MAJsonDeserializer
import com.maproductions.mohamedalaa.core.internal.MAJsonSerializer
import com.maproductions.mohamedalaa.core.java.GsonConverter
import com.maproductions.mohamedalaa.annotation._AnnotationsConstants
import com.maproductions.mohamedalaa.core.internal.UriTypeAdapter
import java.lang.reflect.Field
import java.lang.reflect.Type
import kotlin.reflect.full.declaredFunctions

/**
 * - Same as [Gson.toJson] isa, **But** might make a change to the output of the json value
 * to be able in future to deserialize what was impossible to be deserialized
 * (an abstract class instance for example) isa.
 *
 * @see toJsonOrNull
 * @see fromJson
 * @see fromJsonOrNull
 */
inline fun <reified E> E?.toJson(gson: Gson? = null): String = this?.run {
    val usedGson = gson ?: privateGeneratedGson

    object : GsonConverter<E>(usedGson){}.toJson(this)
} ?: throw RuntimeException("Can't convert `null` to JSON String")

/**
 * - Same as [toJson] but returns `null` instead of throwing an exception isa.
 *
 * @see toJson
 * @see fromJson
 * @see fromJsonOrNull
 */
inline fun <reified E> E?.toJsonOrNull(gson: Gson? = null): String? = runCatching {
    toJson<E>(gson)
}.getOrNull()

/**
 * - Same as [Gson.fromJson] isa, and it can deserialize special serialization done by [toJson],
 * **However** It must be the same [gson] used (whether `null` or non-null same instance) isa.
 *
 * @see fromJsonOrNull
 * @see toJsonOrNull
 * @see toJson
 */
inline fun <reified E> String?.fromJson(gson: Gson? = null): E = this?.run {
    E::class.objectInstance?.apply {
        return@run this
    }

    val usedGson = gson ?: privateGeneratedGson

    object : GsonConverter<E>(usedGson){}.fromJson(this)
} ?: throw RuntimeException("Can't convert `null` to a non-null object of type ${E::class}")

/**
 * - Same as [fromJson] but returns `null` instead of throwing an exception isa.
 *
 * @see fromJson
 * @see toJson
 * @see toJsonOrNull
 */
inline fun <reified E> String?.fromJsonOrNull(gson: Gson? = null): E? = runCatching {
    fromJson<E>(gson)
}.getOrNull()

// ---- Internal fun isa.

/**
 * - List of all annotated classes by the developer using this library's annotation & processor isa.
 */
@Suppress("UNCHECKED_CAST")
internal val allAnnotatedClasses: List<Class<*>> by lazy {
    runCatching {
        val kClass = Class.forName(_AnnotationsConstants.generatedMASealedAbstractOrInterfaceFullName).kotlin

        val function = kClass.declaredFunctions.first()

        function.call(kClass.objectInstance) as List<Class<*>>
    }.getOrNull() ?: emptyList()
}

/**
 * - Default [Gson] object used for serialization/deserialization, the generated object is created by below code isa.
 * ```
 * GsonBuilder()
 *      .serializeNulls()
 *      .setLenient()
 *      .enableComplexMapKeySerialization()
 *      .create()
 * ```
 * - Used lazy property instead of a fun so that computation is only done once isa.
 */
@PublishedApi
internal val privateGeneratedGson: Gson by lazy {
    getLibLikeGeneratedGson()
}

@PublishedApi
internal fun getLibLikeGeneratedGson(
    vararg excludedClassesForTypeAdapters: Class<*>,
    excludedTypesForTypeAdapters: List<Type> = emptyList(),
    excludeSuperclassesOfGivenClasses: Boolean = true
): Gson {
    val gsonBuilder = GsonBuilder()

    val converters = gsonBuilder.addTypeAdapters(
        excludedClassesForTypeAdapters.toList(),
        excludedTypesForTypeAdapters,
        excludeSuperclassesOfGivenClasses
    )

    return gsonBuilder
        .also {
            if (MAGson.useDefaultGsonBuilderConfigs) {
                it.serializeNulls()
                it.setLenient()
                it.enableComplexMapKeySerialization()

                /* -> Leave normal configs
                it.setFieldNamingStrategy { field ->
                    "${field.type.name}\$${field.name}"
                }
                */
            }

            MAGson.gsonBuilderConfigs(it)
        }.create()
        .setUsedGsonInConverters(converters)
}

/**
 * @return JSON key to be used for given [field] by using [Gson.fieldNamingStrategy] and if `null`
 * we use [Field.getName] instead isa.
 */
internal fun Gson.getJsonKey(field: Field): String {
    return fieldNamingStrategy().translateName(field) ?: field.name
}

// ---- Private fun isa.

private fun Gson.setUsedGsonInConverters(converters: List<Pair<MAJsonSerializer, MAJsonDeserializer>>): Gson {
    return also {
        for ((serializer, deserializer) in converters) {
            serializer.usedGson = it
            deserializer.usedGson = it
        }
    }
}

private fun GsonBuilder.addTypeAdapters(
    excludedClassesForTypeAdapters: List<Class<*>> = emptyList(),
    excludedTypesForTypeAdapters: List<Type> = emptyList(),
    excludeSuperclassesOfGivenClasses: Boolean = true
): List<Pair<MAJsonSerializer, MAJsonDeserializer>> {
    // Register special classes isa.
    registerTypeAdapter(Uri::class.java, UriTypeAdapter())

    val types = if (excludeSuperclassesOfGivenClasses) {
        val newAllAnnotatedClasses = allAnnotatedClasses.filterNot { annotatedClass ->
            excludedClassesForTypeAdapters.any { annotatedClass.isAssignableFrom(it) }
        }

        newAllAnnotatedClasses - excludedTypesForTypeAdapters
    }else {
        allAnnotatedClasses - excludedClassesForTypeAdapters - excludedTypesForTypeAdapters
    }

    return types.map {
        val serializer = MAJsonSerializer()
        val deserializer = MAJsonDeserializer()

        registerTypeAdapter(it, serializer)
        registerTypeAdapter(it, deserializer)

        serializer to deserializer
    }
}
