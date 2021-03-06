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

package com.maproductions.mohamedalaa.core3

import com.maproductions.mohamedalaa.annotation.MAAbstract

@MAAbstract
sealed class Core3SealedClass {
    object Object1 : Core3SealedClass()

    data class Data1(
        var string: String = "Hello",
        var float: Float = 5.3f,
        var pair1: Pair<Float, Double> = 3.43f to 43.499,
    ) : Core3SealedClass()
}
