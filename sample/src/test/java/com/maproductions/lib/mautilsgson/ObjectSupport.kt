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

package com.maproductions.lib.mautilsgson

import android.os.Build
import com.google.gson.Gson
import com.maproductions.lib.mautils_gson_core.fromJson
import com.maproductions.lib.mautils_gson_core.toJson
import com.maproductions.lib.mautilsgson.model.SomeObject
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@Suppress("LocalVariableName")
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class ObjectSupport {

    @Test
    fun checkLibraryAndGson() {
        val someObject = SomeObject

        // MAUtils Gson Library
        val json1 = someObject.toJson()
        val someObject1 = json1.fromJson<SomeObject>()

        assertEquals(someObject, someObject1) // Test Passed

        // Gson Library
        val gson = Gson()

        val json2 = gson.toJson(someObject)
        val someObject2 = gson.fromJson(json2, SomeObject::class.java)

        assertNotEquals(someObject, someObject2) // Test Passed
    }

}
