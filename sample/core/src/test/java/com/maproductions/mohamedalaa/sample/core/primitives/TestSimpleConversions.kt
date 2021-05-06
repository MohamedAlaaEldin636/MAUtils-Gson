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

package com.maproductions.mohamedalaa.sample.core.primitives

import com.maproductions.mohamedalaa.coloredconsole.consoleVerboseLn
import org.junit.Test

class TestSimpleConversions {

    @Test
    fun charToStringAndViceVersa() {
        val char = 200.toChar()
        val string = char.toString()
        val r1 = string.single()

        consoleVerboseLn(char)
        consoleVerboseLn(string)
        consoleVerboseLn(r1)
        consoleVerboseLn(string.toCharArray().size)
    }

    @Test
    fun simple() {
        val b = 2.toByte()

        consoleVerboseLn(b)
        consoleVerboseLn(b.toString())
        consoleVerboseLn(b.toString().toByte())
    }

}
