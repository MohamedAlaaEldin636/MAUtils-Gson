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

package com.maproductions.mohamedalaa.core

import androidx.test.platform.app.InstrumentationRegistry
//import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
//import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(Robo::class)
class ExampleInstrumentedTest {

    /*
    todo
    besides test cases in Atom add below isa.

    List<Sealed Class >

    Sealed Class

    Sealed Class has fields of another Sealed Class and self nullable Sealed Class
    and List of self and other sealed class and another annotated not sealed class isa.

    custom class not annotated having field of an annotated sealed class isa.

    include Double and float checks in a single class isa.


    additional field not in the constructor in sef and superclass
     */

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.maproductions.mohamedalaa.core.test", appContext.packageName)
    }
}