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

import com.google.gson.internal.`$Gson$Types`
import com.maproductions.mohamedalaa.core.java.GsonConverter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

inline fun <reified E> E?.getFullTypeInfo(): Type = this?.run {
    object : FullTypeInfo<E>(){}.type
} ?: throw RuntimeException("Can't convert `null` to JSON String")

abstract class FullTypeInfo<E> {

    val type: Type
        get() = getSuperclassTypeParameter(javaClass)

    /** Gets full [Type] info without erasure isa. */
    private fun getSuperclassTypeParameter(subclass: Class<*>): Type {
        val superclass = subclass.genericSuperclass

        if (superclass is Class<*>) {
            throw RuntimeException("Missing type parameter isa.")
        }

        val parameterizedType = superclass as ParameterizedType

        // NOTION -> superclass represents generic type with type params even <? extends Pair> etc...
        // canonicalize represents Type however keeps type parameters correct as well isa.
        // BUT
        // my own canonicalize doesn't keep ? extends while other one does it

        return GsonConverter.canonicalizeOrNull(
            parameterizedType.actualTypeArguments[0]
        ) ?: `$Gson$Types`.canonicalize(parameterizedType.actualTypeArguments[0])
    }

}