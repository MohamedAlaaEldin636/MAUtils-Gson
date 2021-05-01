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

package com.maproductions.mohamedalaa.sample.core

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.maproductions.mohamedalaa.core.*
import com.maproductions.mohamedalaa.core.toJson
import com.maproductions.mohamedalaa.sample.core.normal_gson_same_field_name.OpenClass2
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Suppress("LocalVariableName")
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class NormalGsonConversion {

    private fun Gson.toJsonOrExceptionAsString(src: Any?): String? {
        return kotlin.runCatching { toJson(src) }.getOrElse { "Exception -> $it" }
    }

    @Test
    fun sameSuperClassFieldName() {
        val gson1 = GsonBuilder()
            .serializeNulls()
            .setLenient()
            .enableComplexMapKeySerialization()
            .setFieldNamingStrategy {
                "${it.declaringClass}\$${it.name}"
            }
            .create()

        val gson2 = GsonBuilder()
            .serializeNulls()
            .setLenient()
            .enableComplexMapKeySerialization()
            .create()

        val o1 = OpenClass2()

        println(
            gson1.toJsonOrExceptionAsString(o1)
        )
        // todo make a library for consoleLog colors isa.
        //gson1.fieldNamingStrategy().translateName()
        println(
            gson2.toJsonOrExceptionAsString(o1)
        )
    }

    @Test // sameFieldNameInSuperclass // AndType
    fun check1() {
        /*val gson = GsonBuilder()
            .serializeNulls()
            .setLenient()
            .enableComplexMapKeySerialization()
            .create()*/

        //RuntimeEnvironment.systemContext
        //ApplicationInfo.
        println(ApplicationProvider.getApplicationContext<Application>())

        val openClass2 = OpenClass2()

        val j1 = openClass2.toJsonOrNull()

        val v1 = j1.fromJsonOrNull<OpenClass2>()

        println(j1)
        println(v1)
        println(openClass2 == v1)
        println(openClass2 === v1)
    }

}
