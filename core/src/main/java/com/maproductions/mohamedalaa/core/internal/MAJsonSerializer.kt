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
import com.maproductions.mohamedalaa.coloredconsole.consoleWTFLn
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
internal class MAJsonSerializer : JsonSerializer<Any?> {

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

        // Object, Enum, String, Primitives
        (when {
            src.javaClass.kotlin.isObject -> {
                jsonObject.put(KEY_OBJECT_SERIALIZATION_JSON_STRING, OBJECT_SERIALIZATION)
            }
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
        repeat(4) {
            val newGson = when (it) {
                0,1 -> getLibLikeGeneratedGson(src.javaClass)
                2 -> getLibLikeGeneratedGson(clazz ?: return@repeat)
                else -> getLibLikeGeneratedGson(excludedTypesForTypeAdapters = listOf(type ?: return@repeat))
            }

            val jsonString = when (it) {
                1 -> src.toJsonOrNullJava(src.javaClass, newGson)
                0 -> src.toJsonOrNull(newGson)
                2 -> src.toJsonOrNullJava(clazz, newGson)
                else -> src.toJsonOrNullWithFullTypeInfo(type ?: return@repeat, newGson)
            }
            if (jsonString != null) {
                val newSrc = when (it) {
                    0,1 -> jsonString.fromJsonOrNullJava(src.javaClass, newGson)
                    2 -> jsonString.fromJsonOrNullJava(clazz ?: return@repeat, newGson)
                    else -> jsonString.fromJsonOrNullWithFullTypeInfo(type, newGson)
                }

                if (src == newSrc) {
                    when (it) {
                        0,1 -> jsonObject.put(KEY_CLASS_FULL_NAME, src.javaClass.name)
                        2 -> jsonObject.put(KEY_CLASS_FULL_NAME, clazz)
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

        // Serialize all fields & super classes fields in the same manner as this fun (so recursion) isa.
        val fieldsJsonObject = JSONObject()
        fieldsJsonObject.fill(src)

        jsonObject.put(KEY_CUSTOM_SERIALIZATION_JSON_STRING, fieldsJsonObject)

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
        // todo if src.getFullTypeInfo() always java.lang.Object then no need, else add it in below optional param as well isa.
        // Use consolePrint oe Logcat to check that out isa.
        consoleWTFLn("Checking TODO of src.getFullTypeInfo() -> \n\t" + kotlin.runCatching { src?.getFullTypeInfo() }.getOrNull())

        return innerSerialize(src)?.toJsonElement() ?: JsonNull.INSTANCE
    }

    // todo also test in the very complex conversions how much time via measureTimeMillis the conversion to/from both take isa.
    // todo above of privateGenerateGson isa.

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

            // todo tests on StrangeProperties class instance isa. and check via equality isa.
            // and Sealed<Sealed<Sealed>>> means having type params not just nested sealed classes isa.
            // also test normal type params not annotated as it should work isa, from prev versions isa.
            // and field instance has type params inside a class of sealed of sealed annotated isa.
            // +
            // all tests in Atoom isa.
            // + girhub ex. normal conversion and others as well isa.
            /*
            todo but to test we need to make sure processor is as intented SO
            go fix it first then come back or ensure annotations are correct isa.
             */

            // todo after all tests isa, check if _GsonConverter can be replaces with MATYpes and if in case of errprs
            // then in MATYpes use wildcard.eliminate wildcard fun and also remove _GSONCOnverter as i think it is useless
            // with MATYpes being existent isa.
            putOrThrow(key, jsonObject)
        }
    }

    /*
    Another way
    JSONObject myData = ...
    Gson gson = new Gson();
    JsonElement element = gson.fromJson(myData.toString(), JsonElement.class);
     */
    private fun JSONObject.toJsonElement(): JsonElement = JsonParser.parseString(this.toString())

    private fun JSONObject.putOrThrow(key: String, value: Any?) {
        if (has(key)) {
            throw IllegalArgumentException("Same key for mapping used key ==> $key")
        }

        put(key, value)
    }

}
