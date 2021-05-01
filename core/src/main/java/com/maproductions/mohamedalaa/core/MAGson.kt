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
@file:Suppress("unused")

package com.maproductions.mohamedalaa.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object MAGson {

    internal var useDefaultGsonBuilderConfigs: Boolean = true

    internal var gsonBuilderConfigs: (GsonBuilder) -> Unit = {}

    /**
     * - Can be used in case you want to add your own configurations for example
     * [GsonBuilder.setFieldNamingStrategy] isa.
     *
     * @param plantOnTopOfDefaultConfigs `true` means you keep default [GsonBuilder] configurations
     * and then you apply [adjustments] on it, `false` means you just use the given [adjustments]
     * when building the [Gson] instance (However the type adapters which denoted by annotations
     * will still be registered).
     */
    @JvmStatic
    fun plantGsonBuilderConfigs(adjustments: (GsonBuilder) -> Unit, plantOnTopOfDefaultConfigs: Boolean = true) {
        useDefaultGsonBuilderConfigs = plantOnTopOfDefaultConfigs

        gsonBuilderConfigs = adjustments
    }

    /**
     * - Default [Gson] object used for serialization/deserialization, the generated object is created by below code isa.
     * ```
     * GsonBuilder()
     *      .serializeNulls()
     *      .setLenient()
     *      .enableComplexMapKeySerialization()
     *      .create()
     * ```
     * **Only if [useDefaultGsonBuilderConfigs] is `true` isa.**
     *
     * @return default [Gson] used by the library when you use [toJson] with no args isa.
     */
    @JvmStatic
    fun getLibUsedGson(): Gson {
        return privateGeneratedGson
    }

}
