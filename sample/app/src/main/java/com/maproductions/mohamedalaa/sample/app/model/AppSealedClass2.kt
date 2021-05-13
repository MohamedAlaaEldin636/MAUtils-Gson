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

import com.maproductions.mohamedalaa.annotation.MAAbstract

@MAAbstract
sealed class AppSealedClass2 {

    data class D1(
        var int: Int
    ) : AppSealedClass2()

    sealed class SubSealedClass : AppSealedClass2() {

        object SubO1 : SubSealedClass()

        data class D1(
            var int: Int = 4,
            var string: String = "string",
            var list: List<String> = listOf("hello", "2")
        ) : SubSealedClass()

    }

}