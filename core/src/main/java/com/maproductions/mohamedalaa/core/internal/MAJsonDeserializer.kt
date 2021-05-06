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
import com.maproductions.mohamedalaa.core.KEY_CLASS_FULL_NAME
import com.maproductions.mohamedalaa.core.fromJsonOrNullWithFullTypeInfo
import com.maproductions.mohamedalaa.core.java.fromJsonOrNullJava
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * - Deserializes annotated types isa.
 *
 * @see MAJsonSerializer
 */
class MAJsonDeserializer : JsonDeserializer<Any?> {

    /**
     * - Will be set later after creation of the class isa.
     */
    var usedGson: Gson? = null

    private val nonNullUsedGson by lazy {
        usedGson ?: throw RuntimeException("Developer error code kjfo830")
    }

    private fun innerDeserialize(jsonObject: JSONObject): Any? {
        val fullNameOfClazz: String? = jsonObject.optString(KEY_CLASS_FULL_NAME)
        val fullNameOfType: String? = jsonObject.optString(KEY_TYPE_FULL_NAME)

        val clazz = kotlin.runCatching { Class.forName(fullNameOfClazz.orEmpty()) }.getOrNull()

        // region Object, Enum, String, Primitives
        (jsonObject.opt(KEY_OBJECT_SERIALIZATION_JSON_STRING)?.let {
            clazz!!.kotlin.objectInstance
        } ?: jsonObject.opt(KEY_ENUM_SERIALIZATION_JSON_STRING)?.let {
            val enumString = it.toString()

            clazz!!.enumConstants!!.first { enum -> enum.toString() == enumString }
        } ?: jsonObject.opt(KEY_FLOAT_SERIALIZATION_JSON_STRING)?.let {
            (it as String).toFloat()
        } ?: jsonObject.opt(KEY_CHAR_SERIALIZATION_JSON_STRING)?.let {
            (it as String).single()
        } ?: jsonObject.opt(KEY_BYTE_SERIALIZATION_JSON_STRING)?.let {
            (it as String).toByte()
        } ?: jsonObject.opt(KEY_SHORT_SERIALIZATION_JSON_STRING)?.let {
            (it as String).toShort()
        } ?: jsonObject.opt(KEY_TYPE_JSON_STRING)?.let {
            MATypes.stringToType(it.toString())
        } ?: jsonObject.optAny(KEY_SUPPORTED_TYPE_SERIALIZATION_JSON_STRING))?.also {
            return it
        }
        // endregion

        val jsonString = (jsonObject.opt(KEY_NORMAL_SERIALIZATION_JSON_OBJECT_JSON_STRING) as? JSONObject)?.toString()
            ?: (jsonObject.opt(KEY_NORMAL_SERIALIZATION_JSON_ARRAY_JSON_STRING) as? JSONArray)?.toString()

        return if (jsonString != null) {
            // Can be serialized via normal serialization isa.

            if (clazz != null) {
                val newGson = getLibLikeGeneratedGson(clazz)

                jsonString.fromJsonOrNullJava(clazz, newGson)
            }else {
                val type = MATypes.stringToType(fullNameOfType!!)

                val newGson = getLibLikeGeneratedGson(excludedTypesForTypeAdapters = listOf(type))

                jsonString.fromJsonOrNullWithFullTypeInfo(type, newGson)
            }
        }else {
            // Can be serialized via custom serialization isa.

            val fieldsJsonObject = jsonObject.get(KEY_CUSTOM_SERIALIZATION_JSON_STRING) as JSONObject

            val instanceOfClazz = objenesis.getInstantiatorOf(clazz!!).newInstance()

            instanceOfClazz.fill(fieldsJsonObject)

            return instanceOfClazz
        }
    }

    private fun Any.fill(fieldsJsonObject: JSONObject) {
        for (field in getClassDeclaredFieldsAndSuperclassesDeclaredFields()) {
            val isAccessible = field.isAccessible
            field.isAccessible = true

            val key = nonNullUsedGson.getJsonKey(field)

            val fieldJsonObject = fieldsJsonObject.optJSONObject(key)

            if (fieldJsonObject != null) {
                kotlin.runCatching {
                    field.set(this, innerDeserialize(fieldJsonObject))
                }
            }

            field.isAccessible = isAccessible
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Any? {
        val jsonString = json?.toString()
        if (jsonString.isNullOrEmpty() || json == JsonNull.INSTANCE) {
            return null
        }

        return innerDeserialize(JSONObject(jsonString))
    }

}
