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
import com.maproductions.mohamedalaa.sample.app.extensions.timeOf
import com.maproductions.mohamedalaa.sample.app.utils.BaseGsonTest
import com.maproductions.mohamedalaa.sample.core.simple_custom_classes.SimpleCustomClass1
import com.maproductions.mohamedalaa.sample.core.simple_custom_classes.SimpleCustomClass2
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@Suppress("LocalVariableName")
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class GsonCustomClassesWithTypeArgsDiffSetup : BaseGsonTest() {

    //region Properties

    private val pair1 = 4.3f to "String"

    private val pair2 = 3 to 2.toByte()

    private val simpleCustomClass1N1 = SimpleCustomClass1(
        "value", 32
    )
    private val simpleCustomClass1N2 = SimpleCustomClass1(
        "value ssssssssssss", 594
    )

    private val simpleCustomClass2N1 = SimpleCustomClass2(
        "mido 1", 25, 99f, simpleCustomClass1N1, null
    )
    private val simpleCustomClass2N2 = SimpleCustomClass2(
        "mido 2", 25222, 99222f, simpleCustomClass1N2, simpleCustomClass2N1
    )
    private val simpleCustomClass2N3 = SimpleCustomClass2(
        "mido 3", 25993, 339933f, simpleCustomClass1N1, simpleCustomClass2N2
    )

    private val list1 = listOf(
        pair1 to simpleCustomClass2N3,
    )

    private val list2 = listOf(
        pair2 to simpleCustomClass2N2,
    )

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
    fun typeArgsListIsNotAnnotated() {
        // List is NOT an annotated class
        timeOf(300) {
            json = list1.toJson()

            val from: List<Pair<Pair<Float, String>, SimpleCustomClass2>> = json.fromJson()

            assertEquals(list1, from)
        }

        // List is NOT an annotated class
        timeOf(300) {
            json = list2.toJson()

            val from: List<Pair<Pair<Int, Byte>, SimpleCustomClass2>> = json.fromJson()

            assertEquals(list2, from)
        }

        timeOf(300) {
            val list = listOf(2f)
            json = list.toJson()
            val from: List<Float> = json.fromJson()

            assertEquals(list, from)
        }
    }

}
