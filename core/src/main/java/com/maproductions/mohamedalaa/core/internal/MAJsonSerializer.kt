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

import android.os.Process
import com.google.gson.*
import com.maproductions.mohamedalaa.core.*
import com.maproductions.mohamedalaa.core.getClassDeclaredFieldsAndSuperclassesDeclaredFields
import com.maproductions.mohamedalaa.core.java.fromJsonOrNullJava
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * - Serializes Annotated types isa.
 *
 * @see MAJsonDeserializer
 */
internal class MAJsonSerializer : JsonSerializer<Any?> {

    /**
     * - Will be set later after creation of the class isa.
     */
    var usedGson: Gson? = null

    private val nonNullUsedGson by lazy {
        usedGson ?: throw RuntimeException("Developer error code kjfofijre8392830")
    }

    companion object {
        const val OBJECT_SERIALIZATION = "{}"
    }

    /*override */fun testingSerialize(
        src: Any?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        if (src == null) {
            return JsonNull.INSTANCE
        }

        // todo maybe try treating like onDraw since this will get called so many times so make
        // variables creations not here isa.
        val jsonObject = JSONObject()
        jsonObject.put(KEY_CLASS_FULL_NAME, src.javaClass.name)

        /*
        jsonObject.getJSONObject()
        jsonObject.getJSONArray()*/
        when {
            src.javaClass.kotlin.isObject -> {
                jsonObject.put(KEY_OBJECT_SERIALIZATION_JSON_STRING, OBJECT_SERIALIZATION)
            }
            src.javaClass.isEnum -> {
                jsonObject.put(KEY_ENUM_SERIALIZATION_JSON_STRING, src.toString())
            }
            src is Float -> {
                jsonObject.put(KEY_FLOAT_SERIALIZATION_JSON_STRING, src)
            }
            src is Char -> {
                jsonObject.put(KEY_CHAR_SERIALIZATION_JSON_STRING, src)
            }
            src is Byte -> {
                jsonObject.put(KEY_BYTE_SERIALIZATION_JSON_STRING, src)
            }
            src is Short -> {
                jsonObject.put(KEY_SHORT_SERIALIZATION_JSON_STRING, src)
            }
            src is Double || src is Boolean || src is Int || src is Long || src is String -> {
                jsonObject.put(KEY_SUPPORTED_TYPE_SERIALIZATION_JSON_STRING, src)
            }
        }
        // instance
        val instance = 'o'
        val `in` = 'o'
        val _in________________________________________________ = "\\``"
        _in________________________________________________[5]
        /*
        via constructor get names if possible isa.
         */

        //src.javaClass.constructors.first().parameters
        src.javaClass.kotlin.constructors.first().parameters
        val an = src.javaClass.kotlin.constructors.first().parameters.first().name
        src.javaClass.fields.first { it.name == an }
        // or all is leave as is src isa. and use is Number ba2a isa. todo

        if (src.javaClass.kotlin.isObject) {
            jsonObject.put(KEY_OBJECT_SERIALIZATION_JSON_STRING, OBJECT_SERIALIZATION)
        }else {
            // Check if can be serialized via normal serialization isa.
            val newGson = getLibLikeGeneratedGson(src.javaClass)

            var doneSerialization = false

            val jsonValue = src.toJsonOrNull(newGson)
            if (jsonValue != null) {
                val newSrc = jsonValue.fromJsonOrNullJava(src.javaClass, newGson)

                if (src == newSrc) {
                    when (src) {
                        is Boolean, is Number, is String, is Char -> {
                            jsonObject.put(KEY_NORMAL_SERIALIZATION_JSON_STRING, src)
                        }
                        else -> kotlin.runCatching {
                            // todo in deserialization build json given in FULL NAME of class then use from Json isa.
                            jsonObject.put(KEY_NORMAL_SERIALIZATION_JSON_STRING, JSONObject(jsonValue))
                        }.getOrElse {
                            kotlin.runCatching {
                                jsonObject.put(KEY_NORMAL_SERIALIZATION_JSON_STRING, JSONArray(jsonValue))
                            }.getOrElse {
                                return JsonNull.INSTANCE
                            }
                        }
                    }

                    doneSerialization = true
                }
            }

            if (doneSerialization.not()) {
                /** - For fields inside the annotated/provided class isa. */
                val innerJsonObject = JSONObject()
                innerJsonObject.fill(src)

                jsonObject.put(KEY_CUSTOM_SERIALIZATION_JSON_STRING, innerJsonObject)
            }
        }

        //return JsonPrimitive(true)

        return JsonParser.parseString(jsonObject.toString())
    }

    /**
     * - Once this function is reached then [src] is one of the annotated/provided classes or one
     * of their subclasses isa.
     *
     * - Overrides [JsonSerializer.serialize] isa.
     *
     * ### Steps
     *
     * 1. Check if is object (Singleton).
     * 2. Check if can be normally serialized by normal serialization of Gson (without this serializer).
     * 3. serialize all fields in the class isa.
     */
    override fun serialize(
        src: Any?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        if (src == null) {
            return JsonNull.INSTANCE
        }

        val jsonObject = JSONObject()
        jsonObject.put(KEY_CLASS_FULL_NAME, src.javaClass.name)

        if (src.javaClass.kotlin.isObject) {
            jsonObject.put(KEY_CUSTOM_SERIALIZATION_JSON_STRING, OBJECT_SERIALIZATION)
        }else {
            // Check if can be serialized via normal serialization isa.
            val newGson = getLibLikeGeneratedGson(src.javaClass)

            var doneSerialization = false

            val jsonValue = src.toJsonOrNull(newGson)
            if (jsonValue != null) {
                val newSrc = jsonValue.fromJsonOrNullJava(src.javaClass, newGson)

                if (src == newSrc) {
                    when (src) {
                        is Boolean, is Number, is String, is Char -> {
                            jsonObject.put(KEY_NORMAL_SERIALIZATION_JSON_STRING, src)
                        }
                        else -> kotlin.runCatching {
                            // todo in deserialization build json given in FULL NAME of class then use from Json isa.
                            jsonObject.put(KEY_NORMAL_SERIALIZATION_JSON_STRING, JSONObject(jsonValue))
                        }.getOrElse {
                            kotlin.runCatching {
                                jsonObject.put(KEY_NORMAL_SERIALIZATION_JSON_STRING, JSONArray(jsonValue))
                            }.getOrElse {
                                return JsonNull.INSTANCE
                            }
                        }
                    }

                    doneSerialization = true
                }
            }

            if (doneSerialization.not()) {
                /** - For fields inside the annotated/provided class isa. */
                val innerJsonObject = JSONObject()
                innerJsonObject.fill(src)

                jsonObject.put(KEY_CUSTOM_SERIALIZATION_JSON_STRING, innerJsonObject)
            }
        }

        //return JsonPrimitive(true)

        return JsonParser.parseString(jsonObject.toString())
    }

    /**
     * - Value of the object being serialized ex. if annotated class C(var int: Int = 5) then this
     * presents value of that class so json is {"int" = 5} isa.
     *
     * ## field.genericType vs field.type vs fieldInstance.javaClass
     *
     * - Checked by .toString in LogCat isa.
     *
     * ### field.genericType
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
     * ### field.type of above example
     *
     * - returned -> class java.lang.Object, interface java.util.List, class java.lang.String
     *
     * ### fieldInstance.javaClass
     *
     * - returns the value assigned to it which is type of after the equal sign so
     * -> class int, java.util.Arrays$ArrayList, class java.lang.String
     *
     * todo type is a problem also what if type that has type args nested needed GsonConversion isa.
     * then need to get full info like toJson does isa.
     * // if can get full type info then u may use full class name ,,, with the value from conversion isa.
     * // so kinda like the above normal conversion
     * // or make a recursion of above thing isa. but this means register additional type to reach
     * // above functions isa ?!
     *
     * if can get full info about class
     * 1st use from json of generic type or any type u can deserialize from if all can't
     * then 2nd try to deserialize from full info type if true use same mechanism of name and value
     * + try the instance.javaClass isa.
     * else 3rd then either can't serialize or can serialize but can't deserialize so just put
     * whatever is serialized isa.
     * also for 1st try 2 types .type and .generic as well isa.
     */
    private fun JSONObject.fill(src: Any) {

        /*
        same as above appeoach isa,
        key is field strategy naming then have 2 nodes, key "javaClass" it's value "full name"
        value "value" and it is the toJson value bs kda isa.
        unless is String or msh 3aref eh w kda ya3ne isa.

        todo ezan mn fo2 m el awal becomes a loop isa. la2 below needs field w kda isa.
         */

        // todo for field if deserialize of generic type and type not correct try full type info if still
        //  can't then throw can serialize but without deserialization and pass str of serialization isa.

        // todo if same jsonKey exists throw exception same key isa.
        for (field in src.getClassDeclaredFieldsAndSuperclassesDeclaredFields()) {
            val isAccessible = field.isAccessible
            field.isAccessible = true

            val fieldInstance: Any? = kotlin.runCatching { field.get(src) }.getOrNull()

            val jsonKey = nonNullUsedGson.getJsonKey(field)

            if (fieldInstance == null) {
                putOrThrow(jsonKey, null)

                continue
                //todo NOO continue re-set isAccessible isa.
            }

            // todo so besides 4 trials of genericType, type, javaClass, getFullTypeInfo -> use merge of generic and javaClass isa.

            field.genericType
            field.type
            fieldInstance.javaClass
            fieldInstance.javaClass.enumConstants.first().toString()
            fieldInstance.getFullTypeInfo()

            // enum + float -> put a sign saying it is an enum or float isa.
            // if enum we need the .javaClass
            val v = Float::class.javaPrimitiveType
            val q = Float::class.java
            "".fromJsonOrNullJava(Float::class.javaPrimitiveType!!)
            "".fromJsonOrNullJava(Float::class.java)
            getBoolean("")
            optBoolean("")
            //getBooleanOrNull("")
            //field.set()
            //getDouble()

            // todo befoe fullinfo -> also fieldInstance.javaClass, name class as key isa. so json object having 2 keys isa. so key and value isa.
            // so node name as field naming then obj kery and value.
            val jsonValue = fieldInstance.toJsonOrNull(nonNullUsedGson)

            //ezan need fromJsonTypeOrNull isa.

            // todo make .fromJson trials and see if works isa.
            // also see if can use the javaClass approach as well isa.

            // todo does enum works ok isa ?!

            // todo also see if the annotated is enum isa, see normal Gson if can convert it isa.

            "".getFullTypeInfo()

            // todo also see like kotlin object , does enum needs to be checked before anything else
            //  via 2 checks 1st if normal gson supports it , 2nd if annotated -> will I suport it isa ?!
            // but i guess 2nd 1 is correct as i kda kda ba3mel check via toJson and fromJson of normal serialization isa.

            // todo also is there a way to merge .javaClass with .getFullInfo in case of type params isa ?!
            // also can i convert a class of Sealed<Sealed<Sealed>>> // ... yes .javaClass will give the impl
            // but not for the type params isa.
            field.genericType
            field.type

            fieldInstance.javaClass.kotlin.objectInstance
            fieldInstance.javaClass.kotlin.isObject

            putOrThrow(
                jsonKey,
                when (fieldInstance) {
                    null -> null // boolean - double - int - long - any - string
                    is String, is Boolean, is Int, is Long, is Double/*, is Enum<*> ?!*/ -> fieldInstance
                    is Float -> fieldInstance.toDouble()
                    else -> if (jsonValue == null) null else kotlin.runCatching {
                        // what if the field is list of sealed class item msln which needs that change isa.
                        // parent of all is collection and array i guess isa.
                        //Collection and Array and IntArray etc... isa.

                        // todo see if StrangeProperties can be converted successfully with my own annotation and without it isa.

                        if (fieldInstance is Collection<*>) {

                        }
                        if (fieldInstance is Array<*>) {

                        }

                        JSONObject(jsonValue)
                    }.getOrElse {
                        kotlin.runCatching { JSONArray(jsonValue) }.getOrNull()
                    }
                }
            )

            field.isAccessible = isAccessible
        }
    }

}
