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

data class StrangeProperties<T>(
    var v1: T?,
    var v2: List<String>,
    var v3: String,
    var v4: List<T?>,
) {
    companion object {
        fun createInt(): StrangeProperties<Int> {
            return StrangeProperties(
                8,
                listOf("1"),
                "I am string isa.",
                listOf(4),
            )
        }
    }
}
