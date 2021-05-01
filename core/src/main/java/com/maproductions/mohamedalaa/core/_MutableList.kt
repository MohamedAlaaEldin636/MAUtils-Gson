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

/**
 * - Same as [firstOrNull], and if an element is found then [MutableList.removeAt] will be used
 * to remove the found element from the `receiver` list isa.
 */
fun <T> MutableList<T>.firstOrNullWithRemoval(predicate: (T) -> Boolean): T? {
    val index = indexOfFirstOrNull(predicate) ?: return null

    val item = this[index]

    removeAt(index)

    return item
}
