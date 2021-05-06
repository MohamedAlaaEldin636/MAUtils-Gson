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

package com.maproductions.mohamedalaa.sample.core.normal_gson_same_field_name

import com.maproductions.mohamedalaa.annotation.MAAbstract

open class OpenClass1 {
    private val p1: Int = 9
}

open class OpenClass2 : OpenClass1() {
    private val p1: Int = 4

    val p2: Int = 6
}

data class DC1(
    var string: String,
    var int: Int = 9
)

// todo fix processor it didn't add below except with @MAProvider isa.
//@MAAbstract
data class AnnDC2(
    var string: String
)
