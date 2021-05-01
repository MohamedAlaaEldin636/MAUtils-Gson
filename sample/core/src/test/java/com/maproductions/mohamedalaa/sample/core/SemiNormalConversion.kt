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

import android.os.Build
import com.maproductions.mohamedalaa.coloredconsole.consolePrintLn
import com.maproductions.mohamedalaa.coloredconsole.consoleVerboseLn
import com.maproductions.mohamedalaa.core.toJsonOrNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

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

}
