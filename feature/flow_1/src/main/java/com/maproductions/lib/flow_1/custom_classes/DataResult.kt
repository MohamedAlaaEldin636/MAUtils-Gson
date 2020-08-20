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

package com.maproductions.lib.flow_1.custom_classes

sealed class DataResult<T> {

    class Loading<T> : DataResult<T>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Loading<*>) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    data class Success<T>(val value: T) : DataResult<T>()

    data class Success2<T>(val value: T, val list: List<Pair<Pair<Float, Double>, Int>>) : DataResult<T>()

    sealed class Error<T>(open val userErrorMsg: String) : DataResult<T>() {

        data class NoInternetConnection<T>(override val userErrorMsg: String): Error<T>(userErrorMsg)

        data class Unknown<T>(override val userErrorMsg: String): Error<T>(userErrorMsg)

        class CanNotBeFoundOnServer<T>: Error<T>("") {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is CanNotBeFoundOnServer<*>) return false
                return true
            }

            override fun hashCode(): Int {
                return javaClass.hashCode()
            }
        }

    }

}
