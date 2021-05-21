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

package com.maproductions.mohamedalaa.sample.app.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.maproductions.mohamedalaa.annotation.generated_as_internal_helper_package.MAGson
import com.maproductions.mohamedalaa.sample.app.extensions.timeOf
import org.junit.BeforeClass
import kotlin.system.measureTimeMillis

open class BaseGsonTest {

    protected val gson: Gson by lazy {
        GsonBuilder()
            .disableHtmlEscaping()
            .serializeNulls()
            .setLenient()
            .enableComplexMapKeySerialization()
            .create()
    }

    protected var json: String = ""
    protected var from: Any = ""

    companion object {

        @JvmStatic
        @BeforeClass
        fun appSetups() {
            timeOf(750) {
                MAGson.setup()
                MAGson.getLibUsedGson()
            }
        }

    }

}