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

package com.maproductions.mohamedalaa.sample.app.extensions

import com.maproductions.mohamedalaa.coloredconsole.ConsoleLog
import com.maproductions.mohamedalaa.coloredconsole.consolePrintLn
import kotlin.system.measureTimeMillis
import kotlin.test.assertTrue

fun printMeasuredTimeMillis(prefix: String = "took ", suffix: String = " ms", code: () -> Unit) {
    consolePrintLn("$prefix${measureTimeMillis(code)}$suffix", stacktraceLimit = 1)
}

fun measuredTimeMillisAndPrintIt(prefix: String = "took ", suffix: String = " ms", code: () -> Unit): Long {
    val time = measureTimeMillis(code)

    consolePrintLn("$prefix${time}$suffix", stacktraceLimit = 1)

    return time
}

/**
 * @param maxTimeAllowedInMillis doesn't mean it will take that time, but at most that time, Also
 * just used to assert and let me know in future how much time approximately it will take isa.
 */
fun timeOf(maxTimeAllowedInMillis: Long, printTime: Boolean = false, codeBlock: () -> Unit) {
    val time = measureTimeMillis(codeBlock)

    if (printTime) {
        println(ConsoleLog.reset + "took " + time + " ms")
    }

    assertLessThanOrEqual(maxTimeAllowedInMillis, time)
}

/**
 * - Asserts that [number] is >= [lessThanNumberOrEqual] isa.
 */
fun <N> assertLessThanOrEqual(number: N, lessThanNumberOrEqual: N) where N : Number, N : Comparable<N> {
    assertTrue(number >= lessThanNumberOrEqual, "number $number is not >= lessThanNumberOrEqual $lessThanNumberOrEqual")
}
