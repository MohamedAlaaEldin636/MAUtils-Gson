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

package com.maproductions.mohamedalaa.core.internal

import com.google.gson.*
import com.maproductions.mohamedalaa.core.*
import com.maproductions.mohamedalaa.core.getClassDeclaredFieldsAndSuperclassesDeclaredFields
import com.maproductions.mohamedalaa.core.java.fromJsonOrNullJava
import com.maproductions.mohamedalaa.core.java.toJsonOrNullJava
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Field
import java.lang.reflect.Type

/**
 * - Serializes Annotated types isa.
 *
 * @see MAJsonDeserializer
 */
internal class MAJsonSerializer(private val baseType: Type) : JsonSerializer<Any?> {

    companion object {
        const val OBJECT_SERIALIZATION = "{}"
    }

    /**
     * - Will be set later after creation of the class isa.
     */
    var usedGson: Gson? = null

    private val nonNullUsedGson by lazy {
        usedGson ?: throw RuntimeException("Developer error code kjfofijre8392830")
    }

    /**
     * - Used by [serialize] & [fill] isa.
     */
    private fun innerSerialize(
        src: Any?,
        clazz: Class<*>? = null,
        type: Type? = null,
    ): JSONObject? {
        if (src == null) {
            return null
        }

        val jsonObject = JSONObject()
        jsonObject.put(KEY_CLASS_FULL_NAME, src.javaClass.name)

        // Enum, String, Primitives
        (when {
            src.javaClass.isEnum -> {
                jsonObject.put(KEY_ENUM_SERIALIZATION_JSON_STRING, src.toString())
            }
            src is Float -> {
                jsonObject.put(KEY_FLOAT_SERIALIZATION_JSON_STRING, src.toString())
            }
            src is Char -> {
                jsonObject.put(KEY_CHAR_SERIALIZATION_JSON_STRING, src.toString())
            }
            src is Byte -> {
                jsonObject.put(KEY_BYTE_SERIALIZATION_JSON_STRING, src.toString())
            }
            src is Short -> {
                jsonObject.put(KEY_SHORT_SERIALIZATION_JSON_STRING, src.toString())
            }
            src is String || src is Double || src is Boolean || src is Int || src is Long -> {
                jsonObject.put(KEY_SUPPORTED_TYPE_SERIALIZATION_JSON_STRING, src)
            }
            src is Type -> {
                jsonObject.put(KEY_TYPE_JSON_STRING, MATypes.typeToString(src))
            }
            else -> null
        })?.also {
            return jsonObject
        }

        // Check if can be serialized via normal serialization isa.
        repeat(3) {
            if (it == 1 && clazz == src.javaClass) {
                return@repeat
            }

            val newGson = when (it) {
                0 -> getLibLikeGeneratedGson(baseType, src.javaClass)
                1 -> getLibLikeGeneratedGson(baseType, clazz ?: return@repeat)
                else -> getLibLikeGeneratedGson(baseType, MATypes.singleTypeForGsonExcludedTypesOrNull(type) ?: return@repeat)
            }

            val jsonString = when (it) {
                0 -> src.toJsonOrNull(newGson)
                1 -> src.toJsonOrNullJava(clazz, newGson)
                else -> src.toJsonOrNullWithFullTypeInfo(type ?: return@repeat, newGson)
            }
            if (jsonString != null) {
                val newSrc = when (it) {
                    0 -> jsonString.fromJsonOrNullJava(src.javaClass, newGson)
                    1 -> jsonString.fromJsonOrNullJava(clazz ?: return@repeat, newGson)
                    else -> jsonString.fromJsonOrNullWithFullTypeInfo(type, newGson)
                }

                if (src == newSrc) {
                    when (it) {
                        0 -> jsonObject.put(KEY_CLASS_FULL_NAME, src.javaClass.name)
                        1 -> jsonObject.put(KEY_CLASS_FULL_NAME, clazz)
                        else -> {
                            jsonObject.remove(KEY_CLASS_FULL_NAME)

                            jsonObject.put(KEY_TYPE_FULL_NAME, MATypes.typeToString(type ?: return@repeat))
                        }
                    }

                    (kotlin.runCatching {
                        jsonObject.put(KEY_NORMAL_SERIALIZATION_JSON_OBJECT_JSON_STRING, JSONObject(jsonString))
                    }.getOrElse {
                        kotlin.runCatching {
                            jsonObject.put(KEY_NORMAL_SERIALIZATION_JSON_ARRAY_JSON_STRING, JSONArray(jsonString))
                        }.getOrNull()
                    })?.also {
                        return jsonObject
                    }
                }
            }
        }

        // Object (might take less than 100 ms in first invocation that's why checked here)
        src.javaClass.objectInstance()?.also {
            jsonObject.put(KEY_OBJECT_SERIALIZATION_JSON_STRING, OBJECT_SERIALIZATION)
            return jsonObject
        }

        // Serialize all fields & super classes fields in the same manner as this fun (so recursion) isa.
        val fieldsJsonObject = JSONObject()
        fieldsJsonObject.fill(src)

        jsonObject.put(KEY_CUSTOM_SERIALIZATION_JSON_STRING, fieldsJsonObject)

        // Any of below two return null happens in case of List<*> while items subtypes of object
        // don't know if there is any other case, however if in future wanna solve such a thing
        // then 1st root not item must be annotated so List, 2nd have additional key in json
        // to indicate I will serialize whole thing by myself 3rd check if list/array/intArray...
        // 4th on items if is a subtype of type args then treat item as if was annotated isa.
        try {
            if (src != jsonObject.toString().fromJsonOrNullJava(src.javaClass)) {
                return null
            }
        }catch (throwable: Throwable) {
            return null
        }

        return jsonObject
    }

    /**
     * ### What is this
     *
     * - Once this function is reached then [src] is one of the annotated/provided classes or one
     * of their subclasses isa.
     *
     * - Overrides [JsonSerializer.serialize] isa.
     *
     * ### Steps
     *
     * - Check [innerSerialize]
     */
    override fun serialize(
        src: Any?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return innerSerialize(src, type = MATypes.canonicalizeOrNull(typeOfSrc))
            ?.toJsonElement() ?: JsonNull.INSTANCE
    }

    /**
     * - fills `receiver` with fields of [src] class serialized as in [innerSerialize] isa.
     *
     * ## field.genericType vs field.type vs fieldInstance.javaClass
     *
     * - Checked by .toString in LogCat isa.
     *
     * ### [Field.getGenericType]
     *
     * - Declared by developer See below Ex.
     * ``` kotlin
     * class<T>(
     *      var field1: T = 4,
     *      var field2: List<String> = listOf(1),
     *      var field3: String = "I am string not even char sequence isa."
     * )
     * ```
     *
     * - then types returned of fields are T, java.util.List<java.lang.String>, class java.lang.String
     *
     * ### [Field.getType]
     *
     * - returned -> class java.lang.Object, interface java.util.List, class java.lang.String
     *
     * ### [Any.javaClass] ( fieldInstance.javaClass )
     *
     * - returns the value assigned to it which is type of after the equal sign so
     * -> class int, java.util.Arrays$ArrayList, class java.lang.String
     *
     * ### Why not using getFullTypeInfo with Fields
     *
     * - cuz on reflection erasure already happened so no data will be retrieved of the type isa.
     */
    private fun JSONObject.fill(src: Any) {
        for (field in src.getClassDeclaredFieldsAndSuperclassesDeclaredFields()) {
            val isAccessible = field.isAccessible
            field.isAccessible = true

            val fieldInstance: Any? = kotlin.runCatching { field.get(src) }.getOrNull()

            val key = nonNullUsedGson.getJsonKey(field)

            val jsonObject = innerSerialize(fieldInstance, field.type, field.genericType)

            field.isAccessible = isAccessible

            putOrThrow(key, jsonObject)
        }
    }

    /**
     * ### Another approach for conversion
     * ```
     * Another way
     * JSONObject myData = ...
     * Gson gson = new Gson();
     * JsonElement element = gson.fromJson(myData.toString(), JsonElement.class);
     * ```
     */
    private fun JSONObject.toJsonElement(): JsonElement = JsonParser.parseString(this.toString())

    private fun JSONObject.putOrThrow(key: String, value: Any?) {
        if (has(key)) {
            throw IllegalArgumentException("Same key for mapping used key ==> $key")
        }

        put(key, value)
    }

}
