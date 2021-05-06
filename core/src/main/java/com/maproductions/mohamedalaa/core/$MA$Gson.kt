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

@file:JvmName("MAGson")
@file:Suppress("unused", "ClassName")

package com.maproductions.mohamedalaa.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * - Shouldn't be accessed except by the library it's in it, But for specific reason it's made public.
 */
object `$MA$Gson` {

    /**
     * - Shouldn't be accessed except by the library it's in it, But for specific reason it's made public.
     */
    var useDefaultGsonBuilderConfigs: Boolean = true

    /**
     * - Shouldn't be accessed except by the library it's in it, But for specific reason it's made public.
     */
    var gsonBuilderConfigs: (GsonBuilder) -> Unit = {}

    /**
     * - Shouldn't be accessed except by the library it's in it, But for specific reason it's made public.
     */
    var allAnnotatedClasses: List<Class<*>> = emptyList()

    /**
     * - Shouldn't be accessed except by the library it's in it, But for specific reason it's made public.
     */
    var checkObjectDeclarationEvenIfNotAnnotated = false

    /**
     * - Shouldn't be accessed except by the library it's in it, But for specific reason it's made public.
     */
    fun getLibUsedGson(): Gson {
        return privateGeneratedGson
    }

}
