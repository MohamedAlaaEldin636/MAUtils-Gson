package com.maproductions.lib.mautils_gson_core.internal

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.maproductions.lib.mautils_gson_core.*
import com.maproductions.lib.mautils_gson_core.declaredFieldsForSuperclassesOnly
import com.maproductions.lib.mautils_gson_core.fromJsonOrNullWithFullTypeInfo
import com.maproductions.lib.mautils_gson_core.java.fromJsonOrNullJava
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance

/**
 * @see MAJsonSerializer
 */
class MAJsonDeserializer : JsonDeserializer<Any?> {

    // todo del this later isa.
    private var performLog = false

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Any? {
        Log.d("Special C12", "Enter deserializer with json $json")

        val jsonAsString = json?.toString()
        if (jsonAsString.isNullOrEmpty()) {
            return null
        }

        val jsonObject = runCatching {
            JSONObject(jsonAsString)
        }.getOrElse {
            Log.e("Lib code 321", "STRANGE UN-EXPECTED isa.")
            return jsonAsString.fromJsonOrNullWithFullTypeInfo(typeOfT)
        }

        if (jsonObject.has(KEY_CLASS_FULL_NAME).not()) {
            return jsonAsString.fromJsonOrNullWithFullTypeInfo(typeOfT)
        }

        val classFullName = runCatching { jsonObject.getString(KEY_CLASS_FULL_NAME) }
            .getOrNull()

        performLog = classFullName?.contains("Error") == true
        if (performLog) {
            Log.v("Special C12", "classFullName $classFullName")
        }

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

        if (performLog) {
            Log.d("Special C12", "serializationJsonObject JSONObject $serializationJsonObject ||||| jClass $jClass")
        }

        if (KEY_CLASS_FULL_NAME in serializationJsonObject.toString()
            && KEY_NORMAL_SERIALIZATION_JSON_STRING in serializationJsonObject.toString()) {
            Log.w("Lib code 321", "Found in deserialization a nesting case isa.")
            if (performLog) {
                Log.d("Special C12", "perform nesting deserialization due to annotation isa, jClass $jClass, context $context")
            }
            return serializationJsonObject.extract(jClass, context)
        }

        if (performLog) {
            Log.i("Special C12", "serializationJsonObject.toString().fromJsonOrNullJava(jClass) ${serializationJsonObject.toString().fromJsonOrNullJava(jClass)} " +
                    "||||||||||||||||||||||  ${serializationJsonObject.toString().fromJsonOrNullWithFullTypeInfo(jClass)}")
            val nS = serializationJsonObject.toString().replace("{", "").replace("}", "")
            Log.i("Special C12", "$nS |||||||| ${nS.fromJsonOrNullJava(jClass)} " +
                    "||||||||||||||||||||||  ${nS.fromJsonOrNullWithFullTypeInfo(jClass)}")
        }else {
            Log.i("Special C13", "Special C13 -> others isa. ${serializationJsonObject.toString().fromJsonOrNullWithFullTypeInfo(jClass)}")
        }

        // todo e3mel elle mawgod ta7t check nta el constructors isa.
        return serializationJsonObject.toString().fromJsonOrNullJava(jClass) ?: runCatching {
            // Get all fields of the jClass that exists in receiver's keys isa.
            val allKeys = serializationJsonObject.keys()
            val newParamsWithKeys = mutableListOf<Pair<Any?, String>>()
            for (index in 0 until serializationJsonObject.length()) {
                val key = allKeys.next()
                val value = runCatching { serializationJsonObject.get(key) }.getOrNull()
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

            for (constructor in jClass.constructors) {
                runCatching {
                    val size = constructor.parameterTypes.size

                    val constructorParams = newParamsWithKeys.subList(0, size).map { it.first }
                    val otherParamsWithKeys = newParamsWithKeys.subList(size, newParamsWithKeys.size)

                    if (performLog) {
                        Log.w("Special C12", "constructor params ${constructor.parameterTypes.toList()} ||| " +
                                "${constructor.genericParameterTypes.toList()} ||| " +
                                "${constructor.typeParameters.toList()}")
                        Log.e("Special C12", "size $size, constructorParams $constructorParams, otherParamsWithKeys $otherParamsWithKeys")
                    }

                    return constructor.newInstance(*constructorParams.toTypedArray()).apply {
                        if (performLog) {
                            Log.e("Special C12", "hmmm $this")
                        }

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

            TODO()
        }.getOrNull()
    }

    /**
     * - Deserialize fields isa.
     */
    private fun JSONObject.extract(
        jClass: Class<out Any>,
        context: JsonDeserializationContext?
    ): Any? {
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

        if (performLog) {
            Log.i("Special C12", "allKeys $allKeys, newParamsWithKeys $newParamsWithKeys")
        }

        // Call appropriate constructor then inject fields isa.
        runCatching {
            for (constructor in jClass.constructors) {
                runCatching {
                    val size = constructor.parameterTypes.size

                    val constructorParams = newParamsWithKeys.subList(0, size).map { it.first }
                    val otherParamsWithKeys = newParamsWithKeys.subList(size, newParamsWithKeys.size)

                    if (performLog) {
                        Log.w("Special C12", "constructor params ${constructor.parameterTypes.toList()} ||| " +
                                "${constructor.genericParameterTypes.toList()} ||| " +
                                "${constructor.typeParameters.toList()}")
                        Log.e("Special C12", "size $size, constructorParams $constructorParams, otherParamsWithKeys $otherParamsWithKeys")
                    }

                    return constructor.newInstance(*constructorParams.toTypedArray()).apply {
                        if (performLog) {
                            Log.e("Special C12", "hmmm $this")
                        }

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

            runCatching {
                return jClass.kotlin.createInstance().apply {
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
            }.getOrElse {
                for (constructor in jClass.kotlin.constructors) {
                    val size = constructor.parameters.size

                    val constructorParams = newParamsWithKeys.subList(0, size).map { it.first }
                    val otherParamsWithKeys = newParamsWithKeys.subList(size, newParamsWithKeys.size)

                    return runCatching {
                        constructor.call(*constructorParams.toTypedArray()).apply {
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
                    }.getOrElse {
                        val map = mutableMapOf<KParameter, Any?>()
                        for (param in constructor.parameters) {
                            if (param.isOptional.not()) {
                                map += param to newParamsWithKeys.firstOrNull { it.second == param.name }
                            }
                        }

                        return constructor.callBy(map).apply {
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
        }

        return null
    }

    private fun String?.containsAnnotationSerialization(): Boolean {
        return if (this == null) false else KEY_CLASS_FULL_NAME in this && KEY_NORMAL_SERIALIZATION_JSON_STRING in this
    }

}
