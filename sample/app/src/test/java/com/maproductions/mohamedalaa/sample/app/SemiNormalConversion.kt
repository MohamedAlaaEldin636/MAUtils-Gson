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

package com.maproductions.mohamedalaa.sample.app

import android.os.Build
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.maproductions.mohamedalaa.coloredconsole.consolePrintLn
import com.maproductions.mohamedalaa.coloredconsole.consoleVerboseLn
import com.maproductions.mohamedalaa.core.toJsonOrNull
import com.maproductions.mohamedalaa.sample.app.model.StrangeProperties
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Type
import kotlin.reflect.full.declaredMemberProperties

@Suppress("LocalVariableName")
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class SemiNormalConversion {

    @Test
    fun emptyString() {
        val empty = ""
        val j1 = empty.toJsonOrNull()

        consoleVerboseLn(j1 ?: "NULL")
    }

    @Test
    fun originalGson() {
        val gson = GsonBuilder()
            .serializeNulls()
            .setLenient()
            .enableComplexMapKeySerialization()
            .create()

        val o1 = StrangeProperties<Any>()
        val j1 = gson.toJson(o1.v1) // todo o1.v1 as Any in case was annotated and diff declaration isa.

        consoleVerboseLn("j1 $j1")
        consoleVerboseLn("o1.v1 ${o1.v1}")
        consoleVerboseLn("o1.v1.javaClass ${o1.v1?.javaClass}")
        //consoleVerboseLn("gson.fromJson(o1) ${gson.fromJson(j1, StrangeProperties::class.declaredMemberProperties.first { it.name == "v1" }.returnType)}")
        consoleVerboseLn("type -> ${StrangeProperties::class.java.declaredFields.first { it.name == "v1" }.type}")
        consoleVerboseLn("genericType -> ${StrangeProperties::class.java.declaredFields.first { it.name == "v1" }.genericType}")
        consoleVerboseLn("from type -> ${gson.fromJson(j1, StrangeProperties::class.java.declaredFields.first { it.name == "v1" }.type)}")
        consoleVerboseLn("from type -> ${gson.fromJson(j1, StrangeProperties::class.java.declaredFields.first { it.name == "v1" }.type) == o1.v1}")
        consoleVerboseLn("o1.v1 is Int -> ${o1.v1 is Int}")
        consoleVerboseLn("Int::class.java -> ${gson.fromJson(j1, Int::class.java) == o1.v1}")
        consoleVerboseLn("Int::class.java -> ${gson.fromJson(j1, o1.v1?.javaClass) == o1.v1}")
        consoleVerboseLn("from generic type -> ${kotlin.runCatching { gson.fromJson<StrangeProperties<Any>>(j1, StrangeProperties::class.java.declaredFields.first { it.name == "v1" }.genericType) }.getOrElse { it.stackTraceToString() }}")
        /*
        tmm ezan
        if type not as javaClass then make special serialization
        BUT
        what if has type params in this case the javaClass correct but generic is correct in type params isa. ex. Mido<AbstractClass> v1,v2 : AbstractClass diff implementation ?!
        test but since Abstract is annotated it should by itself make that serialization and deserialization corect isa.

        ezan use javaClass with type params as the generic type but how to do so isa ?! see Gson ParameterizedTypeImpl isa.
        unless servives conversion isa. -> also ezan in full name it can be parameterized isa.
         */
    }

}
