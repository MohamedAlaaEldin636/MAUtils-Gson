package com.maproductions.lib.mautils_gson_core

import android.util.Log
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.maproductions.lib.mautils_gson_core.internal.canonicalizeOrNull
import com.maproductions.lib.mautils_gson_core.java.GsonConverter
import java.lang.reflect.Field
import java.lang.reflect.Type

internal fun Any.getClassDeclaredFieldsAndSuperclassesDeclaredFields(): List<Field> {
    return javaClass.declaredFieldsForSuperclassesOnly(javaClass.declaredFields.filterNotNull())
}

internal fun Any?.toJsonOrNullWithFullTypeInfo(genericType: Type, gson: Gson? = null): String? = this?.run {
    val usedGson = gson?.addTypeAdapters() ?: privateGeneratedGson

    try { object : GsonConverterWithFullTypeInfo(genericType, usedGson){}.toJsonOrNull(this) } catch (e: Exception) { null }
}

internal fun String?.fromJsonOrNullWithFullTypeInfo(genericType: Type?, gson: Gson? = null): Any? = this?.run {
    if (genericType == null) {
        return@run null
    }

    val usedGson = gson?.addTypeAdapters() ?: privateGeneratedGson

    try { object : GsonConverterWithFullTypeInfo(genericType, usedGson){}.fromJsonOrNull(this) } catch (e: Exception) { null }
}

internal abstract class GsonConverterWithFullTypeInfo(
    private val genericType: Type,
    private val gson: Gson? = null
) {

    fun <E> toJsonOrNull(element: E?): String? {
        val type = GsonConverter.canonicalizeOrNull(genericType) ?: `$Gson$Types`.canonicalize(genericType)

        val usedGson = gson?.addTypeAdapters() ?: privateGeneratedGson

        return try { usedGson.toJson(element, type) } catch (e: Exception) { null }
    }

    fun <E> fromJsonOrNull(json: String?): E? {
        val type = GsonConverter.canonicalizeOrNull(genericType) ?: `$Gson$Types`.canonicalize(genericType)

        Log.e("Special C12", "Special C12 - genericType $genericType")
        Log.e("Special C12", "Special C12 - type $type")
        Log.e("Special C12", "Special C12 - 1 ${GsonConverter.canonicalizeOrNull(genericType)}")
        Log.e("Special C12", "Special C12 - 2 ${`$Gson$Types`.canonicalize(genericType)}")

        (type as? Class<*>)?.kotlin?.objectInstance?.apply {
            @Suppress("UNCHECKED_CAST")
            return this as E?
        }

        val usedGson = gson?.addTypeAdapters() ?: privateGeneratedGson

        return try { usedGson.fromJson(json, type) } catch (e: Exception) { null }
    }

}
