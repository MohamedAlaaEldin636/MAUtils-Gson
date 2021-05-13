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
import com.google.gson.reflect.TypeToken
import com.maproductions.mohamedalaa.core.fromJson
import com.maproductions.mohamedalaa.core.toJson
import com.maproductions.mohamedalaa.sample.app.utils.BaseGsonTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@Suppress("LocalVariableName")
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class GsonFlaws : BaseGsonTest() {

    data class HolderOfPrimitives(
        var int: Int,
        var float: Float,
        var long: Long,
        var double: Double,
        var short: Short,
        var byte: Byte,
        var char: Char,
        var boolean: Boolean,
    )

    private val holderOfPrimitives = HolderOfPrimitives(
        3,
        4.5f,
        99L,
        89.72,
        2,
        3,
        'S',
        true
    )

    private val pair1 = 3.4f to "str"

    /**
     * - No flaws
     */
    @Test
    fun primitives() {
        var json: String
        var from: Any

        val int = 1
        json = gson.toJson(int)
        from = gson.fromJson(json, Int::class.java)
        assertEquals(from, int)

        val float = 1f
        json = gson.toJson(float)
        from = gson.fromJson(json, Float::class.java)
        assertEquals(from, float)

        val long = 1L
        json = gson.toJson(long)
        from = gson.fromJson(json, Long::class.java)
        assertEquals(from, long)

        val double = 1.0
        json = gson.toJson(double)
        from = gson.fromJson(json, Double::class.java)
        assertEquals(from, double)

        val short = 1.toShort()
        json = gson.toJson(short)
        from = gson.fromJson(json, Short::class.java)
        assertEquals(from, short)

        val byte = 1.toByte()
        json = gson.toJson(byte)
        from = gson.fromJson(json, Byte::class.java)
        assertEquals(from, byte)

        val char = 'a'
        json = gson.toJson(char)
        from = gson.fromJson(json, Char::class.java)
        assertEquals(from, char)

        val boolean = true
        json = gson.toJson(boolean)
        from = gson.fromJson(json, Boolean::class.java)
        assertEquals(from, boolean)
    }

    /**
     * - Requires use of [TypeToken] in case of type args isa.
     */
    @Test
    fun fieldsOfPrimitives() {
        json = gson.toJson(holderOfPrimitives)
        from = gson.fromJson(json, HolderOfPrimitives::class.java)
        assertEquals(from, holderOfPrimitives)

        json = gson.toJson(pair1)
        from = gson.fromJson(json, Pair::class.java)
        assertNotEquals(from, pair1)

        val typeToken = object : TypeToken<Pair<Float, String>>(){}.type

        json = gson.toJson(pair1, typeToken)
        from = gson.fromJson(json, typeToken)
        assertEquals(from, pair1)
    }
    /**
     * - No need for [TypeToken], see corresponding [fieldsOfPrimitives].
     */
    @Test
    fun myLib_fieldsOfPrimitives() {
        json = holderOfPrimitives.toJson()
        from = json.fromJson<HolderOfPrimitives>()
        assertEquals(from, holderOfPrimitives)

        json = pair1.toJson()
        from = json.fromJson<Pair<Float, String>>()
        assertEquals(from, pair1)
    }

}
