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

@file:JvmName("GsonUtils")
@file:Suppress("unused")

package com.maproductions.mohamedalaa.core.java

import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.maproductions.mohamedalaa.core.*
import com.maproductions.mohamedalaa.core.internal.MATypes
import com.maproductions.mohamedalaa.core.privateGeneratedGson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * - Same as [Gson.toJson] isa, **But** might make a change to the output of the json value
 * to be able in future to deserialize what was impossible to be deserialized
 * (an abstract class instance for example) isa.
 *
 * ### CAUTION
 * 1. This should only be used by java consumer code isa.
 * 2. If the type has type params then you have to use [GsonConverter] instead isa.
 *
 * @see toJsonOrNullJava
 * @see fromJsonJava
 * @see fromJsonOrNullJava
 */
@JvmOverloads
@JvmName("toJson")
fun <E> E?.toJsonJava(elementClass: Class<*>? = null, gson: Gson? = null): String {
    val usedGson = gson ?: privateGeneratedGson

    val value = when {
        this == null -> null
        elementClass == null -> usedGson.toJson(this)
        else -> usedGson.toJson(this, elementClass)
    }

    return if (value != null && value != "null") {
        value
    }else {
        throw RuntimeException("Can't convert $this to JSON String")
    }
}

/**
 * - Same as [toJsonJava] but returns `null` instead of throwing an exception isa.
 *
 * @see toJsonJava
 * @see fromJsonOrNullJava
 * @see fromJsonJava
 */
@JvmOverloads
@JvmName("toJsonOrNull")
fun <E> E?.toJsonOrNullJava(elementClass: Class<*>? = null, gson: Gson? = null): String? = runCatching {
    toJsonJava(elementClass, gson)
}.getOrNull()

/**
 * - Same as [Gson.fromJson] isa, and it can deserialize special serialization done by [toJsonJava],
 * **However** It must be the same [gson] used (whether `null` or non-null same instance) isa.
 *
 * ### CAUTION
 * 1. This should only be used by java consumer code isa.
 * 2. If the type has type params then you have to use [GsonConverter] instead isa.
 *
 * @see fromJsonOrNullJava
 * @see toJsonJava
 * @see toJsonOrNullJava
 */
@JvmOverloads
@JvmName("fromJson")
fun <E> String?.fromJsonJava(elementClass: Class<E>, gson: Gson? = null): E {
    if (this != null) {
        if (`$MA$Gson`.checkObjectDeclarationEvenIfNotAnnotated) {
            (elementClass as? Class<*>)?.objectInstance()?.apply {
                @Suppress("UNCHECKED_CAST")
                (this as? E)?.also { return it }
            }
        }
    }

    val usedGson = gson ?: privateGeneratedGson

    val value = if (this == null) null else usedGson.fromJson(this, elementClass)

    return value ?: throw RuntimeException("Can't convert $this to object of type $elementClass")
}

/**
 * - Same as [fromJsonJava] but returns `null` instead of throwing an exception isa.
 *
 * @see fromJsonJava
 * @see toJsonOrNullJava
 * @see toJsonJava
 */
@JvmOverloads
@JvmName("fromJsonOrNull")
fun <E> String?.fromJsonOrNullJava(elementClass: Class<E>, gson: Gson? = null): E? = runCatching {
    fromJsonJava(elementClass, gson)
}.getOrNull()

/**
 * ### CAUTION
 *
 * - Currently java consumer code is NOT supported, Check out GitHub README of this library isa.
 *
 * - Should be used only for java consumer code, Not for kotlin consumer code which instead should
 * use [fromJsonOrNull], [fromJson], [toJsonOrNull] OR [toJson] isa.
 *
 * ### Description
 * - Used only if your Object that needs to be converted to/from JSON-String has type parameters isa,
 * Otherwise consider using `GsonUtils` functions -> [fromJsonOrNullJava], [fromJsonJava],
 * [toJsonOrNullJava] OR [toJsonJava] isa.
 *
 * ### How to use
 * ```
 * // Java consumer used one time (if needed more check below so that you don't repeat same code)
 * CustomWithTypeParam<CustomObject, Integer> customWithTypeParam = new CustomWithTypeParam<>();
 *
 * GsonConverter<CustomWithTypeParam<CustomObject, Integer>> gsonConverter
 *      = new GsonConverter<CustomWithTypeParam<CustomObject, Integer>>() {};
 *
 * String jsonString = gsonConverter.toJsonOrNull(customWithTypeParam);
 * CustomWithTypeParam<CustomObject, Integer> fromJsonObject = gsonConverter.fromJsonOrNull(jsonString);
 *
 * assertEquals(customWithTypeParam, fromJsonObject) // true.
 * ```
 *
 * @param gson in case you want a special configuration for [Gson], Note default value used is
 * [`$MA$Gson`.getLibUsedGson] isa.
 *
 * @param E type to convert to/from JSON String.
 */
abstract class GsonConverter<E>(private val gson: Gson? = null) {

    internal companion object;

    /**
     * - Same as [Gson.toJson] isa.
     *
     * @throws RuntimeException in case of any error while converting isa.
     */
    fun toJson(element: E): String? {
        val type = getSuperclassTypeParameter(javaClass)

        val usedGson = gson ?: privateGeneratedGson

        return usedGson.toJson(element, type)
    }

    /**
     * - Same as [Gson.fromJson] isa.
     */
    fun fromJson(json: String): E? {
        val type = getSuperclassTypeParameter(javaClass)

        val usedGson = gson ?: privateGeneratedGson

        return usedGson.fromJson(json, type)
    }

    /** Gets full [Type] info without erasure for conversion using [Gson] isa. */
    private fun getSuperclassTypeParameter(subclass: Class<*>): Type {
        val superclass = subclass.genericSuperclass

        if (superclass is Class<*>) {
            throw RuntimeException("Missing type parameter isa.")
        }

        val parameterizedType = superclass as ParameterizedType

        return MATypes.canonicalizeOrNullAndEliminateWildcardTypes(
            parameterizedType.actualTypeArguments[0]
        ) ?: `$Gson$Types`.canonicalize(parameterizedType.actualTypeArguments[0])
    }

}
