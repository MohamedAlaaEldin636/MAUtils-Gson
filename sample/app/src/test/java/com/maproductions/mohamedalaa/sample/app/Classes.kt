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
import com.maproductions.mohamedalaa.coloredconsole.consoleDebugLn
import com.maproductions.mohamedalaa.coloredconsole.consolePrintLn
import com.maproductions.mohamedalaa.coloredconsole.consoleVerboseLn
import com.maproductions.mohamedalaa.core.fromJsonOrNull
import com.maproductions.mohamedalaa.core.internal.FullTypeInfo
import com.maproductions.mohamedalaa.core.internal.getFullTypeInfo
import com.maproductions.mohamedalaa.core.java.fromJsonOrNullJava
import com.maproductions.mohamedalaa.core.toJsonOrNull
import com.maproductions.mohamedalaa.sample.app.model.Enum1
import com.maproductions.mohamedalaa.sample.app.model.SealedClass1
import com.maproductions.mohamedalaa.sample.app.model.StrangeProperties
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.ParameterizedType

@Suppress("LocalVariableName")
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class Classes {

    @Test
    fun otherSpecialEffects() {
        val e = Enum1.S1
        val j = e.toJsonOrNull()
        val r = j.fromJsonOrNull<Enum1>()
        val rr = j.fromJsonOrNullJava(Enum1::class.java)

        consoleDebugLn(e)
        consoleDebugLn(j)
        consoleDebugLn(r)
        consoleDebugLn(rr)
    }

    @Test
    fun fullTypeInfo() {
        val sealedClass1: SealedClass1 = SealedClass1.SealedClass2.SubClass2(3)

        val v1 = listOf(
            5 to listOf(
                Triple(" ", 5.4, sealedClass1)
            )
        )

        consolePrintLn(v1.getFullTypeInfo())
        //consolePrintLn(v1 is Collection) // Always true

        val o1 = StrangeProperties<Any>()

        consoleVerboseLn("a ${o1.v2.getFullTypeInfo()}")
        consoleVerboseLn("aa ${o1.v3.getFullTypeInfo()}")
        consoleVerboseLn("aaa ${o1.getFullTypeInfo()}")

        consoleVerboseLn("o1.v1 ${o1.v1}")
        consoleVerboseLn("o1.v1 ${o1.v1.getFullTypeInfo()}")
        consoleVerboseLn("o1.v1.javaClass ${o1.v1?.javaClass}")
        consoleVerboseLn("type -> ${StrangeProperties::class.java.declaredFields.first { it.name == "v1" }.type}")
        consoleVerboseLn("genericType -> ${StrangeProperties::class.java.declaredFields.first { it.name == "v1" }.genericType}")
        consoleVerboseLn("o1.v1 is Int -> ${o1.v1 is Int}")

        //o1.v4.getFullTypeInfo().javaClass.declaringClass
        o1.v4.javaClass.declaringClass // owner type
        o1.v4.javaClass // raw type
        (o1.v4.getFullTypeInfo() as ParameterizedType).actualTypeArguments // actualTypeArguments
        // todo so besides 4 trials of genericType, type, javaClass, getFullTypeInfo -> use merge of generic and javaClass isa.

        consoleVerboseLn("o1.v4 ${o1.v4}")
        consoleVerboseLn("o1.v4 ${o1.v4.getFullTypeInfo()}")
        consoleVerboseLn("o1.v4 ${(o1.v4.getFullTypeInfo() as? ParameterizedType)?.actualTypeArguments?.toList()}")
        consoleVerboseLn("o1.v4 ${(o1.v4.getFullTypeInfo() as? ParameterizedType)?.rawType}")
        consoleVerboseLn("o1.v4 ${(o1.v4.getFullTypeInfo() as? ParameterizedType)?.ownerType}")
        consoleVerboseLn("o1.v4.javaClass ${o1.v4.javaClass}")
        consoleVerboseLn("type -> ${StrangeProperties::class.java.declaredFields.first { it.name == "v4" }.type}")
        consoleVerboseLn("genericType -> ${StrangeProperties::class.java.declaredFields.first { it.name == "v4" }.genericType}")
        consoleVerboseLn("o1.v4 is Int -> ${o1.v4 is List<*>}")
        // javaClass is more rational isa.
        // can i combine javaClass with full info isa.
    }

    @Test
    fun infoInStrangeProps() {
        
    }

    enum class APPEARANCE {
        HIDE, SHOW(), UNKNOWN
    }

    @Test
    fun actualIsAssignable() {
        /*SealedClass1::class.java.isAssignableFrom(

        )*/
        APPEARANCE.HIDE.toString()
        APPEARANCE.valueOf("")
    }

}
