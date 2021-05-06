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

@file:Suppress("unused")

package com.maproductions.mohamedalaa.core

import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import com.google.gson.Gson
import java.io.Serializable

/**
 * - Puts 1 [value] with given [key] in a bundle, **If** [value]'s [Class] is supported by [Bundle]
 * then corresponding put function will be used (like [Bundle.putString], [Bundle.putInt] etc... ),
 * **Otherwise** [Bundle.putString] will be used where value is the result of [value] + [String.toJsonOrNull]
 * with given [gson] as parameter isa.
 *
 * - Note if [value] is `null` then we will use [Bundle.putString] with `null` value isa,
 * so on retrieving the value the Type must be nullable isa.
 *
 * @param gson if `null` then default [Gson] instance will be used which can be retrieved via
 * [`$MA$Gson`.getLibUsedGson] isa.
 */
fun Bundle.putJson(key: String?, value: Any?, gson: Gson? = null) {
    addValueAndGetErrorMsgIfNotAdded(key, value)?.also {
        putString(key, value.toJsonOrNull(gson))
    }
}

/**
 * @return [T] instance after being casted if wasn't saved as a json string or after being
 * converted by [fromJsonOrNull] if saved as json string or `null` if not found isa,
 */
inline fun <reified T> Bundle.getJsonOrNull(key: String?, gson: Gson? = null): T? {
    return when (val any = get(key)) {
        is String -> when {
            T::class == String::class -> any as? T
            else -> any.fromJsonOrNull<T>(gson)
        }
        else -> any as? T
    }
}

/**
 * - Same as [getJsonOrNull] but instead of returning `null`, a [RuntimeException]
 * is thrown instead isa.
 */
inline fun <reified T> Bundle.getJson(key: String?, gson: Gson? = null): T = getJsonOrNull<T>(key, gson)
    ?: throw RuntimeException("Cannot get ${T::class} from key $key")

/**
 * - Puts 1 [value] with given [key] in a bundle.
 *
 * - Note if [value] is `null` then we will use [Bundle.putString] with `null` value isa,
 * so on retrieving the value the Type must be nullable isa.
 *
 * @return error msg or `null` if put value successfully isa.
 */
internal fun Bundle.addValueAndGetErrorMsgIfNotAdded(key: String?, value: Any?): String? {
    when(value) {
        null -> putString(key, null) // Any nullable type will suffice.

        // Primitives
        is Boolean -> putBoolean(key, value)
        is Byte -> putByte(key, value)
        is Char -> putChar(key, value)
        is Double -> putDouble(key, value)
        is Float -> putFloat(key, value)
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
        is Short -> putShort(key, value)

        // Other
        is CharSequence -> putCharSequence(key, value) // contains ( String )
        is Bundle -> putBundle(key, value)
        is Parcelable -> putParcelable(key, value)

        // Primitives arrays
        is BooleanArray -> putBooleanArray(key, value)
        is ByteArray -> putByteArray(key, value)
        is CharArray -> putCharArray(key, value)
        is DoubleArray -> putDoubleArray(key, value)
        is FloatArray -> putFloatArray(key, value)
        is IntArray -> putIntArray(key, value)
        is LongArray -> putLongArray(key, value)
        is ShortArray -> putShortArray(key, value)

        // Other arrays
        is Array<*> -> {
            val componentType = value::class.java.componentType
                ?: return "Illegal value array type null for key \"$key\""

            @Suppress("UNCHECKED_CAST") // Checked by reflection.
            when {
                Parcelable::class.java.isAssignableFrom(componentType) -> {
                    // contains ( SparseArray )
                    putParcelableArray(key, value as Array<Parcelable>)
                }
                String::class.java.isAssignableFrom(componentType) -> {
                    putStringArray(key, value as Array<String>)
                }
                CharSequence::class.java.isAssignableFrom(componentType) -> {
                    putCharSequenceArray(key, value as Array<CharSequence>)
                }
                Serializable::class.java.isAssignableFrom(componentType) -> {
                    putSerializable(key, value)
                }
                else -> {
                    val valueType = componentType.canonicalName

                    return "Illegal value array type $valueType for key \"$key\""
                }
            }
        }

        // Serializable contains ( Enum, All Arrays )
        is Serializable -> putSerializable(key, value)

        else -> {
            @Suppress("UNCHECKED_CAST")
            if (Build.VERSION.SDK_INT >= 18 && value is IBinder) {
                putBinder(key, value)
            }else if (Build.VERSION.SDK_INT >= 21 && value is Size) {
                putSize(key, value)
            }else if (Build.VERSION.SDK_INT >= 21 && value is SizeF) {
                putSizeF(key, value)
            }else if (value is SparseArray<*> && value as? SparseArray<Parcelable> != null) {
                putSparseParcelableArray(key, value)
            }else {
                val valueType = value.javaClass.canonicalName

                return "Illegal value type $valueType for key \"$key\""
            }
        }
    }

    return null
}
