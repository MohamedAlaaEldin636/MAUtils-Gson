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

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.maproductions.mohamedalaa.core.*
import com.maproductions.mohamedalaa.core.KEY_CLASS_FULL_NAME
import com.maproductions.mohamedalaa.core.KEY_NORMAL_SERIALIZATION_JSON_STRING
import com.maproductions.mohamedalaa.core.declaredFieldsForSuperclassesOnly
import com.maproductions.mohamedalaa.core.fromJsonOrNullWithFullTypeInfo
import com.maproductions.mohamedalaa.core.java.fromJsonOrNullJava
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance

/**
 * @see MAJsonSerializer
 */
class MAJsonDeserializer : JsonDeserializer<Any?> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Any? {
        val jsonAsString = json?.toString()
        if (jsonAsString.isNullOrEmpty()) {
            return null
        }

        val jsonObject = runCatching {
            JSONObject(jsonAsString)
        }.getOrElse {
            return jsonAsString.fromJsonOrNullWithFullTypeInfo(typeOfT)
        }

        if (jsonObject.has(KEY_CLASS_FULL_NAME).not()) {
            return jsonAsString.fromJsonOrNullWithFullTypeInfo(typeOfT)
        }

        val classFullName = runCatching { jsonObject.getString(KEY_CLASS_FULL_NAME) }
            .getOrNull()

        /*
        ==> (Just FYI) In case of DataResult.Loading
        - runCatching { classFullName?.let { Class.forName(it) } }.getOrNull() == DataResult.Loading
        - typeOfT == com.maproductions.lib.flow_1.custom_classes.DataResult<com.maproductions.mohamedalaa.core.model.UICountry>
        - typeOfT as? Class<*> == null
        - jClass.kotlin.objectInstance null
         */
        val jClass = runCatching { classFullName?.let { Class.forName(it) } }.getOrNull()
            ?: typeOfT as? Class<*> ?: return null // type is unknown so impossible deserialization isa.

        jClass.kotlin.objectInstance?.apply {
            return this
        }

        val serializationJsonObject = runCatching {
            jsonObject.getJSONObject(KEY_NORMAL_SERIALIZATION_JSON_STRING)
        }.getOrNull() ?: return null // data is unknown so impossible deserialization isa.

        if (serializationJsonObject.toString().containsAnnotationSerialization()) {
            return serializationJsonObject.extract(jClass, context)
        }

        return serializationJsonObject.toString().fromJsonOrNullJava(jClass) ?: serializationJsonObject.extract(jClass, context)
    }

    /**
     * - Deserialize fields isa.
     */
    private fun JSONObject.extract(
        jClass: Class<out Any>,
        context: JsonDeserializationContext?
    ): Any? = runCatching {
        // Get all fields of the jClass that exists in receiver's keys isa.
        val allKeys = keys()
        val newParamsWithKeys = mutableListOf<Pair<Any?, String>>()
        for (index in 0 until length()) {
            val key = allKeys.next()
            val value = runCatching { get(key) }.getOrNull()
            val valueAsString = value.toStringOrEmpty()

            val fieldClass = if (value !is JSONObject && value !is JSONArray) null else jClass.runCatching {
                declaredFieldsForSuperclassesOnly(declaredFields.toList()).firstOrNull {
                    it.name == key
                }?.genericType
            }.getOrNull()

            val newValue = when {
                value is JSONObject && value.toString().containsAnnotationSerialization() -> {
                    deserialize(
                        runCatching { JsonParser.parseString(valueAsString) }.getOrNull(),
                        fieldClass,
                        context
                    )
                }
                value is JSONObject || value is JSONArray -> {
                    val jsonElement = runCatching { JsonParser.parseString(valueAsString) }.getOrNull()
                    if (context != null && jsonElement != null && fieldClass != null) {
                        context.deserialize(
                            jsonElement,
                            fieldClass
                        )
                    }else {
                        value.toString().fromJsonOrNullWithFullTypeInfo(fieldClass)
                    }
                }
                else -> value
            }

            newParamsWithKeys += newValue to key
        }

        // Call appropriate constructor then inject fields isa.
        for (constructor in jClass.constructors) {
            runSafely {
                val size = constructor.parameterTypes.size

                val constructorParams = newParamsWithKeys.subList(0, size).map { it.first }
                val otherParamsWithKeys = newParamsWithKeys.subList(size, newParamsWithKeys.size)

                return@runCatching constructor.newInstance(*constructorParams.toTypedArray()).apply {
                    if (otherParamsWithKeys.isNotEmpty()) {
                        for ((param, key) in otherParamsWithKeys) {
                            try {
                                val field = runCatching {
                                    javaClass.getDeclaredFieldsForSelfAndSuperclassesOnly(key)
                                }.getOrNull() ?: continue

                                val isAccessible = field.isAccessible
                                field.isAccessible = true

                                field.set(this, param)

                                field.isAccessible = isAccessible
                            }catch (throwable: Throwable) {
                                // ignore isa.
                            }
                        }
                    }
                }
            }
        }

        runSafely {
            return@runCatching jClass.kotlin.createInstance().apply {
                for ((param, key) in newParamsWithKeys) {
                    try {
                        val field = runCatching {
                            javaClass.getDeclaredFieldsForSelfAndSuperclassesOnly(key)
                        }.getOrNull() ?: continue

                        val isAccessible = field.isAccessible
                        field.isAccessible = true

                        field.set(this, param)

                        field.isAccessible = isAccessible
                    }catch (throwable: Throwable) {
                        // ignore isa.
                    }
                }
            }
        }

        for (constructor in jClass.kotlin.constructors) {
            runSafely {
                val size = constructor.parameters.size

                val constructorParams = newParamsWithKeys.subList(0, size).map { it.first }
                val otherParamsWithKeys = newParamsWithKeys.subList(size, newParamsWithKeys.size)

                runSafely {
                    return@runCatching constructor.call(*constructorParams.toTypedArray()).apply {
                        if (otherParamsWithKeys.isNotEmpty()) {
                            for ((param, key) in otherParamsWithKeys) {
                                try {
                                    val field = runCatching {
                                        javaClass.getDeclaredFieldsForSelfAndSuperclassesOnly(key)
                                    }.getOrNull() ?: continue

                                    val isAccessible = field.isAccessible
                                    field.isAccessible = true

                                    field.set(this, param)

                                    field.isAccessible = isAccessible
                                }catch (throwable: Throwable) {
                                    // ignore isa.
                                }
                            }
                        }
                    }
                }

                val map = mutableMapOf<KParameter, Any?>()
                for (param in constructor.parameters) {
                    if (param.isOptional.not()) {
                        map += param to newParamsWithKeys.firstOrNull { it.second == param.name }
                    }
                }

                if (map.size != size) {
                    return@runCatching constructor.callBy(map).apply {
                        for ((param, key) in otherParamsWithKeys) {
                            try {
                                val field = runCatching {
                                    javaClass.getDeclaredFieldsForSelfAndSuperclassesOnly(key)
                                }.getOrNull() ?: continue

                                val isAccessible = field.isAccessible
                                field.isAccessible = true

                                field.set(this, param)

                                field.isAccessible = isAccessible
                            }catch (throwable: Throwable) {
                                // ignore isa.
                            }
                        }
                    }
                }
            }
        }

        null
    }.getOrNull()

    private fun String?.containsAnnotationSerialization(): Boolean {
        return if (this == null) false else {
            KEY_CLASS_FULL_NAME in this && KEY_NORMAL_SERIALIZATION_JSON_STRING in this
        }
    }

}
