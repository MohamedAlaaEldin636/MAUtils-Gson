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
import com.maproductions.mohamedalaa.annotation.generated_as_internal_helper_package.MAGson
import com.maproductions.mohamedalaa.coloredconsole.consolePrintLn
import com.maproductions.mohamedalaa.core.fromJson
import com.maproductions.mohamedalaa.core.toJson
import com.maproductions.mohamedalaa.sample.app.extensions.printMeasuredTimeMillis
import com.maproductions.mohamedalaa.sample.core.simple_custom_classes.SimpleCustomClass1
import com.maproductions.mohamedalaa.sample.core.simple_custom_classes.SimpleCustomClass2
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@Suppress("LocalVariableName")
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class GsonSimpleCustomClasses {

    private val simpleCustomClass1N1 = SimpleCustomClass1(
        "value", 32
    )
    private val simpleCustomClass1N2 = SimpleCustomClass1(
        "value ssssssssssss", 594
    )

    private val simpleCustomClass2N1 = SimpleCustomClass2(
        "mido 1",25, 99f, simpleCustomClass1N1, null
    )
    private val simpleCustomClass2N2 = SimpleCustomClass2(
        "mido 2",25222, 99222f, simpleCustomClass1N2, simpleCustomClass2N1
    )
    private val simpleCustomClass2N3 = SimpleCustomClass2(
        "mido 2",25333, 339933f, simpleCustomClass1N1, simpleCustomClass2N2
    )

    private val gson by lazy {
        GsonBuilder()
            .serializeNulls()
            .setLenient()
            .enableComplexMapKeySerialization()
            /*.setFieldNamingStrategy {
                "${it.declaringClass}\$${it.name}"
            }*/
            .create()
    }

    @Before
    fun appSetups() {
        MAGson.setup(
            /*checkObjectDeclarationEvenIfNotAnnotated = true*/ // Default is false
        )
    }

    @Test
    fun simpleCustomClass1() = printMeasuredTimeMillis {
        var json = simpleCustomClass1N1.toJson()

        var from = json.fromJson<SimpleCustomClass1>()

        assertEquals(from, simpleCustomClass1N1)

        simpleCustomClass1N1.string = "new value"

        json = simpleCustomClass1N1.toJson()

        from = json.fromJson()

        assertEquals(from, simpleCustomClass1N1)

        simpleCustomClass1N1.string = simpleCustomClass1N2.string
        simpleCustomClass1N1.int = simpleCustomClass1N2.int

        json = simpleCustomClass1N1.toJson()

        from = json.fromJson()

        assertEquals(from, simpleCustomClass1N2)
    }

    @Test
    fun simpleCustomClass2() = printMeasuredTimeMillis {
        var json = simpleCustomClass2N3.toJson()

        var from = json.fromJson<SimpleCustomClass2>()

        assertEquals(from, simpleCustomClass2N3)

        simpleCustomClass2N3.string = "new value"
        simpleCustomClass2N3.simpleCustomClass2 = simpleCustomClass2N1

        json = simpleCustomClass2N3.toJson()

        from = json.fromJson()

        assertEquals(from, simpleCustomClass2N3)

        simpleCustomClass2N2.string = simpleCustomClass2N3.string
        simpleCustomClass2N2.int = simpleCustomClass2N3.int
        simpleCustomClass2N2.float = simpleCustomClass2N3.float
        simpleCustomClass2N2.simpleCustomClass1 = simpleCustomClass2N3.simpleCustomClass1
        simpleCustomClass2N2.simpleCustomClass2 = simpleCustomClass2N3.simpleCustomClass2

        json = simpleCustomClass2N3.toJson()

        from = json.fromJson()

        assertEquals(from, simpleCustomClass2N2)
    }

    @Test
    fun simpleCustomClass1ViaNormalGson() = printMeasuredTimeMillis {
        var json = gson.toJson(simpleCustomClass1N1)

        var from = gson.fromJson(json, SimpleCustomClass1::class.java)

        assertEquals(from, simpleCustomClass1N1)

        simpleCustomClass1N1.string = "new value"

        json = gson.toJson(simpleCustomClass1N1)

        from = gson.fromJson(json, SimpleCustomClass1::class.java)

        assertEquals(from, simpleCustomClass1N1)

        simpleCustomClass1N1.string = simpleCustomClass1N2.string
        simpleCustomClass1N1.int = simpleCustomClass1N2.int

        json = gson.toJson(simpleCustomClass1N1)

        from = gson.fromJson(json, SimpleCustomClass1::class.java)

        assertEquals(from, simpleCustomClass1N2)
    }

}
