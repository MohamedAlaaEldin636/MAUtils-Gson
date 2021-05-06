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

package com.maproductions.mohamedalaa.sample.core.types

import com.maproductions.mohamedalaa.coloredconsole.*
import com.maproductions.mohamedalaa.core.internal.getFullTypeInfo
import org.junit.Test

class TestStrangeTypes {

    class StrangeProperties<T> {

        var v1: T? = 8 as? T

        var v2: List<String> = listOf("1")

        var v3: String = "I am string isa."

        var v4: List<T?> = listOf(4 as? T)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is StrangeProperties<*>) return false

            if (v1 != other.v1) return false
            if (v2 != other.v2) return false
            if (v3 != other.v3) return false
            if (v4 != other.v4) return false

            return true
        }

        override fun hashCode(): Int {
            var result = v1?.hashCode() ?: 0
            result = 31 * result + v2.hashCode()
            result = 31 * result + v3.hashCode()
            result = 31 * result + v4.hashCode()
            return result
        }

    }

    @Test
    fun reflectionTypes() {
        val strangeProperties = StrangeProperties<Int>()

        val clazz = strangeProperties.javaClass

        consolePrintLn(strangeProperties.v1.getFullTypeInfo())
        consolePrintLn(strangeProperties.v2.getFullTypeInfo())
        consolePrintLn(strangeProperties.v3.getFullTypeInfo())
        consolePrintLn(strangeProperties.v4.getFullTypeInfo())
        consolePrintLn("")

        for (field in clazz.declaredFields) {
            consolePrintLn("name ${field.name}")
            consoleVerboseLn("genericType ${field.genericType}", stacktraceLimit = 1)
            consoleDebugLn("type ${field.type}", stacktraceLimit = 1)

            var instance: Any? = when (field.name) {
                "v1" -> strangeProperties.v1
                "v2" -> strangeProperties.v2
                "v3" -> strangeProperties.v3
                else -> strangeProperties.v4
            }
            consoleInfoLn("1. javaClass ${instance?.javaClass}", stacktraceLimit = 1)
            consoleWarnLn("1. getFullTypeInfo ${instance?.getFullTypeInfo()}", stacktraceLimit = 1)

            instance = field.let { it.isAccessible = true; it.get(strangeProperties) }
            consoleInfoLn("2. javaClass ${instance.javaClass}", stacktraceLimit = 1)
            consoleWarnLn("2. getFullTypeInfo ${instance.getFullTypeInfo()}", stacktraceLimit = 1)

            consolePrintLn("")
        }
    }

}
