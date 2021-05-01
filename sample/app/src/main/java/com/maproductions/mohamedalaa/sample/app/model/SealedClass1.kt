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

package com.maproductions.mohamedalaa.sample.app.model

sealed class SealedClass1 {

    object O1 : SealedClass1()

    data class SubClass1(
        var int: Int = 0
    ) : SealedClass1()

    sealed class SealedClass2 : SealedClass1() {

        object O2 : SealedClass2()

        data class SubClass2(
            var int: Int = 0
        ) : SealedClass2()

    }

}
