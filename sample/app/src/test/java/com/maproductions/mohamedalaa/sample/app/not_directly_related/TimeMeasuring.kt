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

package com.maproductions.mohamedalaa.sample.app.not_directly_related

import android.os.Build
import com.maproductions.mohamedalaa.annotation.generated_as_internal_helper_package.MAGson
import com.maproductions.mohamedalaa.core.*
import com.maproductions.mohamedalaa.core.internal.MATypes
import com.maproductions.mohamedalaa.core3.Core3SealedClass
import com.maproductions.mohamedalaa.sample.app.extensions.timeOf
import com.maproductions.mohamedalaa.sample.app.model.AnnEnum1
import com.maproductions.mohamedalaa.sample.app.model.Enum1
import com.maproductions.mohamedalaa.sample.app.utils.BaseGsonTest
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Field
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("LocalVariableName")
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class TimeMeasuring : BaseGsonTest() {

    //region Classes & Properties

    @Suppress("MayBeConstant", "unused")
    object O1 {

        val p1 = ""

        @JvmField
        val p2 = ""

        @JvmStatic
        val p3 = ""

    }

    @Suppress("MayBeConstant", "unused")
    enum class E1 {
        START, IN_PROGRESS, END
    }

    @Suppress("MayBeConstant", "unused")
    class C1 {
        @JvmField
        val pair1 = listOf(
            3.2
        ) to Triple(
            4f,
            5,
            listOf(
                "" to 3L
            )
        )
    }

    private val arrayElements10 = arrayOf(
        3, "", 4.3, 5f, 5L, "", "sa", "saijsoaij"
    )

    private val data1 = Core3SealedClass.Data1()

    //endregion

    companion object {
        @JvmStatic
        @BeforeClass
        fun appSetups() {
            timeOf(400) {
                MAGson.setup()
                `$MA$Gson`.allAnnotatedClasses = `$MA$Gson`.allAnnotatedClasses + listOf(
                    Int::class.java, Int::class.javaObjectType
                )
                MAGson.getLibUsedGson()
            }
        }
    }

    @Test
    fun measure_by_my_conversion_allPossibleMAJsonConversionSituations() {
        // Not Annotated Enum
        timeOf(100) {
            val json = Enum1.S1.toJsonOrNull()
            val from = json.fromJsonOrNull<Enum1>()

            assertEquals(Enum1.S1, from)
        }

        // Annotated Enum
        timeOf(100) {
            val json = AnnEnum1.S1.toJsonOrNull()
            val from = json.fromJsonOrNull<AnnEnum1>()

            assertEquals(AnnEnum1.S1, from)
        }

        // Annotated Int
        timeOf(10) {
            val json = 5.toJsonOrNull()
            val from = json.fromJsonOrNull<Int>()

            assertEquals(5, from)
        }

        // Not Annotated Double
        timeOf(10) {
            val json = 5.8.toJsonOrNull()
            val from = json.fromJsonOrNull<Double>()

            assertEquals(5.8, from)
        }

        // instance of subclass (object) of annotated class with specifying the ann class in .toJson
        timeOf(200) {
            json = Core3SealedClass.Object1.toJson<Core3SealedClass>()
            from = json.fromJson<Core3SealedClass>()
            assertEquals(from, Core3SealedClass.Object1)
        }

        // same as above but without specifying the ann class in .toJson
        timeOf(100) {
            json = Core3SealedClass.Object1.toJson()
            from = json.fromJson<Core3SealedClass.Object1>(/*checkObjectDeclaration = true*/)
            assertEquals(from, Core3SealedClass.Object1)
        }

        // instance of subclass (data class) of annotated class with specifying the ann class in .toJson
        timeOf(100) {
            json = data1.toJson<Core3SealedClass>()
            from = json.fromJson<Core3SealedClass>()
            assertEquals(from, data1)
        }

        // same as above but without specifying the ann class in .toJson
        timeOf(100) {
            json = data1.toJson()
            from = json.fromJson<Core3SealedClass.Data1>()
            assertEquals(from, data1)
        }
    }

    @Test
    fun tmpMeasuringTime() {
        timeOf(100) { C1().javaClass.name }

        timeOf(100) { arrayElements10.toList() }

        var fieldPair1: Field? = null
        timeOf(10) {
            fieldPair1 = C1().javaClass.fields.first { it.name == "pair1" }
        }
        timeOf(100) {
            val string = MATypes.typeToString(fieldPair1!!.genericType)

            MATypes.stringToType(string)
        }

        privateTimeOf("", 0, 10)

        privateTimeOf("", 1, 10)
        privateTimeOf(E1.IN_PROGRESS, 1, 10)

        privateTimeOf(O1, 2, 1_000)

        privateTimeOf(O1, 3, 10)
        privateTimeOf("s", 3, 10)

        privateTimeOf("s", 4, 10)
    }

    /**
     * @param maxTimeAllowedInMillis doesn't mean it will take that time, but just used to assert and let
     * me know in future how much time approximately it will take isa.
     */
    private fun privateTimeOf(any: Any, checkNo: Int, maxTimeAllowedInMillis: Long) {
        val time = measureTimeMillis {
            when (checkNo) {
                0 -> any.javaClass.isEnum
                1 -> any.javaClass.enumConstants
                2 -> any.javaClass.kotlin.objectInstance
                3 -> any is Int || any is CharArray || any is Float || any is Double || any is String
            }
        }

        assertTrue(maxTimeAllowedInMillis >= time)
    }

}
