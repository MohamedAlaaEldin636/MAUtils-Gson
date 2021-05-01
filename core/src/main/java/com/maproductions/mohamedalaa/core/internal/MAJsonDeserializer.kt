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
import com.maproductions.mohamedalaa.core.KEY_NORMAL_SERIALIZATION_JSON_STRING
import com.maproductions.mohamedalaa.core.declaredFieldsForSuperclassesOnly
import com.maproductions.mohamedalaa.core.fromJsonOrNullWithFullTypeInfo
import com.maproductions.mohamedalaa.core.java.fromJsonJava
import com.maproductions.mohamedalaa.core.java.fromJsonOrNullJava
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance

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

    // enum deserialization             fieldInstance.javaClass.enumConstants.first().toString()
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Any? {
        val jsonAsString = json?.toString()
        if (jsonAsString.isNullOrEmpty() || typeOfT == null || json == JsonNull.INSTANCE) {
            return null
        }

        var newGson = getLibLikeGeneratedGson(excludedTypesForTypeAdapters = listOf(
            typeOfT
        ))

        val jsonObject = runCatching {
            JSONObject(jsonAsString)
        }.getOrElse {
            return jsonAsString.fromJsonOrNullWithFullTypeInfo(typeOfT, newGson)
        }

        if (jsonObject.has(KEY_CLASS_FULL_NAME).not()) {
            return jsonAsString.fromJsonOrNullWithFullTypeInfo(typeOfT, newGson)
        }

        val classFullName = runCatching { jsonObject.getString(KEY_CLASS_FULL_NAME) }.getOrNull()
        /*
        ==> (Just FYI) In case of DataResult.Loading
        - runCatching { classFullName?.let { Class.forName(it) } }.getOrNull() == DataResult.Loading
        - typeOfT == com.maproductions.lib.flow_1.custom_classes.DataResult<com.maproductions.mohamedalaa.core.model.UICountry>
        - typeOfT as? Class<*> == null
        - jClass.kotlin.objectInstance null
         */
        val jClass = runCatching { classFullName?.let { Class.forName(it) } }.getOrNull()
            ?: typeOfT as? Class<*>
            ?: return jsonAsString.fromJsonOrNullWithFullTypeInfo(typeOfT, newGson)

        jClass.kotlin.objectInstance?.apply {
            return this
        }

        newGson = getLibLikeGeneratedGson(jClass)

        if (jsonObject.has(KEY_NORMAL_SERIALIZATION_JSON_STRING)) {
            return when (val value = jsonObject.get(KEY_NORMAL_SERIALIZATION_JSON_STRING)) {
                is JSONObject, is JSONArray -> {
                    value.toString().fromJsonOrNullJava(jClass, newGson)
                }
                else -> value
            }
        }else {
            val innerJsonObject = jsonObject.getJSONObject(KEY_CUSTOM_SERIALIZATION_JSON_STRING)


            // todo
        }

        val serializationJsonObject = runCatching {
            jsonObject.getJSONObject(KEY_NORMAL_SERIALIZATION_JSON_STRING)
        }.getOrNull() ?: return null // data is unknown so impossible deserialization isa.

        // todo check this one contains and extract isa.
        if (KEY_CLASS_FULL_NAME in serializationJsonObject.toString()) {
            return serializationJsonObject.extract(jClass, context)
        }

        return serializationJsonObject.toString().fromJsonOrNullJava(jClass, nonNullUsedGson)
            ?: /*todo this 1 as well check it out isa.*/serializationJsonObject.extract(jClass, context)
    }

    /**
     * - Deserialize fields & create a new instance of [jClass] to return it isa.
     */
    private fun JSONObject.extract(
        jClass: Class<out Any>,
        context: JsonDeserializationContext?
    ): Any? = runCatching {
        // Get all fields of the jClass that exists in receiver's keys isa.
        val allKeys = keys()

        val mapOfSelfParamsWithKeys = mutableMapOf<String, Any?>()
        val mapOfSuperclassesParamsWithKeys = mutableMapOf<String, Any?>()

        val selfParamsWithKeys = mutableListOf<Pair<Any?, String>>()
        val superclassesParamsWithKeys = mutableListOf<Pair<Any?, String>>()

        val selfFields = jClass.declaredFields.toList()
        val superclassesFields = jClass.declaredFieldsForSuperclassesOnly()

        var isSelfField = false
        for (index in 0 until length()) {
            val key = allKeys.next()
            val value: Any? = opt(key)

            val (fieldType, fieldGenericType) = try {
                if (value !is JSONObject && value !is JSONArray) {
                    null to null
                }else {
                    val field = selfFields.firstOrNull {
                        nonNullUsedGson.getJsonKey(it) == key
                    }.also {
                        isSelfField = it != null
                    } ?: superclassesFields.firstOrNull {
                        nonNullUsedGson.getJsonKey(it) == key
                    }

                    field?.type to field?.genericType
                }
            }catch (throwable: Throwable) {
                null to null
            }

            val v: Class<*> = selfFields.first().type

            val newValue = if (value?.toString()?.containsAnnotationSerialization() == true
                || value is JSONObject || value is JSONArray) {
                    //                     >>>> see kdoc of MASerial.fill isa.
                    // todo zawed wa7da without type info "fromJsonOrNullWithFullTypeInfo"
                        // since no fielldInstance.javaClass can be done here isa.
                            // or ba2a in serial if .genricType != .javaClass then encapsulates in FIELDNAME + NORMAL CONVERSION
                                // OR ANOTHER NAMING CONCEPT ISA.
                    /*
                    ALSO

                    if generic prob is T and A use class.type params and get it from 'em
                    but still case of object and int isa...

                    i guess to cover all cases if javaClass not is generic use additional special serialization isa,
                    cuz if u think of it it is same prob as sealed class u know class is Any
                    but impl is int in that case w hakaza isa.

                    bs kda brdo fe el NORMAL SERIALIZATION if src.javaClass not same
                    make same approach as well isa.

                    now we face case of generics ex. generic type is A<B> while .javaClass
                    surely won't provide type parameters due to erasure so what to do
                    1st try gson getting full type info if can't then via reflections
                    get bounds of A & B upper/lower bound to know what B and A is
                    and then use javaClass and type parameters use exactly B and A isa.

                    but what if List < Abstract class > which will be ArrayList of it hmmm....
                    and same if Mido < Abstract Class > etc... isa. haveing 1 w 2 diff imple fields isa.

                    class Mido<AbstractClass1>(var a1: AbstractClass1, var b2: AbstractClass1)

                    see 1stly if above probs solved by Gson itself if not maybe ignore them isa........

                    >>>> see kdoc of MASerial.fill isa.
                     */
                value.toString().fromJsonOrNullWithFullTypeInfo(fieldGenericType, nonNullUsedGson)
                    ?: fieldType?.let { value.toString().fromJsonOrNullJava(fieldType, nonNullUsedGson) }
            }else {
                value
            }

            if (isSelfField) {
                selfParamsWithKeys += newValue to key
            }else {
                superclassesParamsWithKeys += newValue to key
            }
        }

        val allParamsWithKeys = selfParamsWithKeys + superclassesParamsWithKeys

        // Call appropriate constructor then inject fields isa.
        /*for (constructor in jClass.constructors) {
            runSafely {
                val size = constructor.parameterTypes.size

                val constructorParams = selfParamsWithKeys.subList(0, size).map { it.first }
                val otherParamsWithKeys = selfParamsWithKeys.subList(size, selfParamsWithKeys.size).plus(
                    superclassesParamsWithKeys
                )

                return@runCatching constructor.newInstance(*constructorParams.toTypedArray()).apply {
                    if (otherParamsWithKeys.isNotEmpty()) {
                        for ((param, key) in otherParamsWithKeys) {
                            try {
                                val field = runCatching {
                                    javaClass.getDeclaredFieldForSelfAndSuperclassesOnly(key)
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
        }*/

        // Call appropriate constructor then inject fields isa.
        for (constructor in jClass.constructors) {
            runSafely {
                val size = constructor.parameterTypes.size

                /*constructor.parameters.first().name

                java.time.LocalDate.now()*/

                val constructorParams = selfParamsWithKeys.subList(0, size).map { it.first }
                val otherParamsWithKeys = selfParamsWithKeys.subList(size, selfParamsWithKeys.size).plus(
                    superclassesParamsWithKeys
                )

                return@runCatching constructor.newInstance(*constructorParams.toTypedArray()).apply {
                    if (otherParamsWithKeys.isNotEmpty()) {
                        for ((param, key) in otherParamsWithKeys) {
                            try {
                                val field = runCatching {
                                    javaClass.getDeclaredFieldForSelfAndSuperclassesOnly(key)
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
                //jClass.kotlin.constructors.first().parameters.first().name
                for ((param, key) in allParamsWithKeys) {
                    try {
                        val field = runCatching {
                            javaClass.getDeclaredFieldForSelfAndSuperclassesOnly(key)
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

                val constructorParams = selfParamsWithKeys.subList(0, size).map { it.first }
                val otherParamsWithKeys = selfParamsWithKeys.subList(size, selfParamsWithKeys.size).plus(
                    superclassesParamsWithKeys
                )

                runSafely {
                    return@runCatching constructor.call(*constructorParams.toTypedArray()).apply {
                        if (otherParamsWithKeys.isNotEmpty()) {
                            for ((param, key) in otherParamsWithKeys) {
                                try {
                                    val field = runCatching {
                                        javaClass.getDeclaredFieldForSelfAndSuperclassesOnly(key)
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
                        map += param to allParamsWithKeys.firstOrNull { it.second == param.name }
                    }
                }

                if (map.size != size) {
                    return@runCatching constructor.callBy(map).apply {
                        for ((param, key) in otherParamsWithKeys) {
                            try {
                                val field = runCatching {
                                    javaClass.getDeclaredFieldForSelfAndSuperclassesOnly(key)
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
