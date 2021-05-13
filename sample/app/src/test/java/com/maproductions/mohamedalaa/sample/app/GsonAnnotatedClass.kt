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
import com.maproductions.mohamedalaa.annotation.generated_as_internal_helper_package.MAGson
import com.maproductions.mohamedalaa.core.`$MA$Gson`
import com.maproductions.mohamedalaa.core.fromJson
import com.maproductions.mohamedalaa.core.toJson
import com.maproductions.mohamedalaa.sample.app.extensions.printMeasuredTimeMillis
import com.maproductions.mohamedalaa.sample.app.extensions.timeOf
import com.maproductions.mohamedalaa.sample.app.model.AppSealedClass2
import com.maproductions.mohamedalaa.sample.app.utils.BaseGsonTest
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@Suppress("LocalVariableName")
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class GsonAnnotatedClass : BaseGsonTest() {

    //region Properties

    private val appSealedClass1: AppSealedClass = AppSealedClass.O1
    private val appSealedClass2: AppSealedClass = AppSealedClass.D1("string 1")
    private val appSealedClass3: AppSealedClass = AppSealedClass.D2("string 2", 2.2)
    private val o1OfAppSealedClass: AppSealedClass.O1 = AppSealedClass.O1
    private val d1OfAppSealedClass: AppSealedClass.D1 = AppSealedClass.D1("s 1")
    private val d2OfAppSealedClass: AppSealedClass.D2 = AppSealedClass.D2("s 2", 8974.9)

    private val appSealedClassSubSealedClass1SubO1: AppSealedClass = AppSealedClass.SubSealedClass1.SubO1
    private val appSealedClass2SubSealedClassD1: AppSealedClass2 = AppSealedClass2.SubSealedClass.D1()
    private val appSealedClassSubSealedClass1SubSubSealedClass2SubSubD1: AppSealedClass = AppSealedClass.SubSealedClass1.SubSubSealedClass2.SubSubD1(
        appSealedClassSubSealedClass1SubO1,
        appSealedClass2SubSealedClassD1
    )

    private val appSealedClassSubSealedClass1SubO2 = AppSealedClass.SubSealedClass1.SubO1
    private val appSealedClass2SubSealedClassD2 = AppSealedClass2.SubSealedClass.D1()
    private val appSealedClassSubSealedClass1SubSubSealedClass2SubSubD2 = AppSealedClass.SubSealedClass1.SubSubSealedClass2.SubSubD1(
        appSealedClassSubSealedClass1SubO1,
        appSealedClass2SubSealedClassD1
    )

    private data class Custom1(
        var float: Float,
        var appSealedClass: AppSealedClass,
        var double: Double
    )

    private val custom1 = Custom1(3.4f, appSealedClassSubSealedClass1SubSubSealedClass2SubSubD1, 4.553)

    //endregion

    companion object {
        @JvmStatic
        @BeforeClass
        fun appSetups() {
            timeOf(400) {
                MAGson.setup()
                `$MA$Gson`.allAnnotatedClasses = `$MA$Gson`.allAnnotatedClasses - List::class.java
                MAGson.getLibUsedGson()
            }
        }
    }

    @Test
    fun appSealedClass() = printMeasuredTimeMillis {
        var json = appSealedClass1.toJson()

        var from = json.fromJson<AppSealedClass>()

        assertEquals(appSealedClass1, from)

        json = appSealedClass3.toJson()

        from = json.fromJson()

        assertEquals(appSealedClass3, from)
    }

    @Test
    fun subClassesOfAppSealedClass() = printMeasuredTimeMillis {
        json = appSealedClassSubSealedClass1SubSubSealedClass2SubSubD1.toJson()
        from = json.fromJson<AppSealedClass>()

        assertEquals(appSealedClassSubSealedClass1SubSubSealedClass2SubSubD1, from)

        val list = listOf(
            appSealedClassSubSealedClass1SubSubSealedClass2SubSubD1
        )
        json = list.toJson()
        from = json.fromJson<List<AppSealedClass>>()

        assertEquals(list, from)

        val pair = list to appSealedClassSubSealedClass1SubSubSealedClass2SubSubD1
        json = pair.toJson()
        from = json.fromJson<Pair<List<AppSealedClass>, AppSealedClass>>()

        assertEquals(pair, from)

        json = custom1.toJson()
        from = json.fromJson<Custom1>()

        assertEquals(custom1, from)
    }

    /**
     * - Doesn't specify type as the class annotated but a subclass of it in the .toJson/.fromJson isa
     */
    @Test
    fun subClassesOfAppSealedClass2() = printMeasuredTimeMillis {
        json = appSealedClassSubSealedClass1SubSubSealedClass2SubSubD2.toJson()
        from = json.fromJson<AppSealedClass.SubSealedClass1.SubSubSealedClass2.SubSubD1>()

        assertEquals(appSealedClassSubSealedClass1SubSubSealedClass2SubSubD2, from)

        val list = listOf(
            appSealedClassSubSealedClass1SubSubSealedClass2SubSubD2
        )
        json = list.toJson()
        from = json.fromJson<List<AppSealedClass.SubSealedClass1.SubSubSealedClass2.SubSubD1>>()

        assertEquals(list, from)

        val pair =
            list to appSealedClassSubSealedClass1SubSubSealedClass2SubSubD2
        json = pair.toJson()
        from = json.fromJson<Pair<List<AppSealedClass.SubSealedClass1.SubSubSealedClass2.SubSubD1>, AppSealedClass.SubSealedClass1.SubSubSealedClass2.SubSubD1>>()

        assertEquals(pair, from)

        json = custom1.toJson()
        from = json.fromJson<Custom1>()

        assertEquals(custom1, from)
    }

}
