package com.maproductions.lib.mautils_gson_core.internal

import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.maproductions.lib.mautils_gson_core.allAnnotatedClasses
import com.maproductions.lib.mautils_gson_core.getClassDeclaredFieldsAndSuperclassesDeclaredFields
import com.maproductions.lib.mautils_gson_core.runSafely
import com.maproductions.lib.mautils_gson_core.toJsonOrNullWithFullTypeInfo
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Field
import java.lang.reflect.Type

/**
 * - Annotations are for abstract class, sealed class & interface, but in below kdocs they might be
 * referenced as classes not classes & interface isa.
 */
internal class MAJsonSerializer : JsonSerializer<Any?> {

    /**
     * - Once this function is reached then src is one of the subclasses of the annotated/provided
     * classes isa.
     */
    override fun serialize(
        src: Any?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        Log.w("Lib code 321", "entered serialize with $src, $typeOfSrc, $context")

        if (src == null) {
            return null
        }

        /** - For fields inside the annotated/provided class isa. */
        val innerJsonObject = JSONObject()

        innerJsonObject.fill(src, context)

        val jsonObject = JSONObject()
        jsonObject.put(KEY_CLASS_FULL_NAME, src.javaClass.name)
        jsonObject.put(KEY_NORMAL_SERIALIZATION_JSON_STRING, innerJsonObject)

        return runCatching { JsonParser.parseString(jsonObject.toString()) }.getOrNull()
    }

    /**
     * - Value of the object being serialized ex. if annotated class C(var int: Int = 5) then this
     * presents value of that class so json is {"int" = 5} isa.
     */
    private fun JSONObject.fill(
        src: Any,
        context: JsonSerializationContext?,
    ): Unit = runSafely {
        for (field in src.getClassDeclaredFieldsAndSuperclassesDeclaredFields()) {
            val isAccessible = field.isAccessible
            field.isAccessible = true

            val fieldInstance = field.get(src)

            val jsonKey = field.name

            val jsonValue = field.getJsonValue(fieldInstance, context)
            Log.d("Lib code 321", "adjusted jsonValue $jsonValue")
            put(
                jsonKey,
                when (fieldInstance) {
                    null -> null
                    is String, is Boolean, is Int, is Long, is Double, is Enum<*> -> fieldInstance
                    else -> if (jsonValue == null) null else runCatching {
                        JSONObject(jsonValue)
                    }.getOrElse {
                        runCatching { JSONArray(jsonValue) }.getOrNull()
                    }
                }
            )

            field.isAccessible = isAccessible
        }
    }

    /**
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
     * - todo mayeb if returns T as generic check src which has that generic mahowa class bydefine it isa bs how isa ?!
     * this to-do is for normal conversion see how it only rely on field.genericType so test case
     * where it would return T and value is 1 of the annotated ones, if fails see above suggested to-do isa.
     */
    private fun Field.getJsonValue(
        fieldInstance: Any?,
        context: JsonSerializationContext?,
    ): String? {
        if (fieldInstance == null) {
            return null
        }

        return if (fieldInstance.javaClass.isOneOfTheAnnotatedOrOneOfTheSubclassesOfTheAnnotatedClasses()) {
            Log.d("Lib code 321", "nested serialization")
            // Make this go to serialize to convert itself and it's fields isa.
            serialize(fieldInstance, fieldInstance.javaClass, context)?.toString()
        }else {
            // Normal gson conversion isa. // todo check that this won't make me call above serialize fun isa.
            Log.d("Lib code 321", "oneshot")
            context?.serialize(fieldInstance, fieldInstance.javaClass)?.toString()
                ?: fieldInstance.toJsonOrNullWithFullTypeInfo(genericType)
        }

    }

    private fun Class<*>.isOneOfTheAnnotatedOrOneOfTheSubclassesOfTheAnnotatedClasses(): Boolean {
        return allAnnotatedClasses.any {
            it.isAssignableFrom(this)
        }
    }

}
