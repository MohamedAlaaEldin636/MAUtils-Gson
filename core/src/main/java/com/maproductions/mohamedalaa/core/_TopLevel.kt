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

import org.objenesis.ObjenesisStd

internal const val KEY_CLASS_FULL_NAME = "KEY_CLASS_FULL_NAME"
internal const val KEY_TYPE_FULL_NAME = "KEY_TYPE_FULL_NAME"

internal const val KEY_OBJECT_SERIALIZATION_JSON_STRING = "KEY_OBJECT_SERIALIZATION_JSON_STRING"
internal const val KEY_ENUM_SERIALIZATION_JSON_STRING = "KEY_ENUM_SERIALIZATION_JSON_STRING"
internal const val KEY_FLOAT_SERIALIZATION_JSON_STRING = "KEY_FLOAT_SERIALIZATION_JSON_STRING"
internal const val KEY_SUPPORTED_TYPE_SERIALIZATION_JSON_STRING = "KEY_SUPPORTED_TYPE_SERIALIZATION_JSON_STRING"
internal const val KEY_CHAR_SERIALIZATION_JSON_STRING = "KEY_CHAR_SERIALIZATION_JSON_STRING"
internal const val KEY_BYTE_SERIALIZATION_JSON_STRING = "KEY_BYTE_SERIALIZATION_JSON_STRING"
internal const val KEY_SHORT_SERIALIZATION_JSON_STRING = "KEY_SHORT_SERIALIZATION_JSON_STRING"
internal const val KEY_TYPE_JSON_STRING = "KEY_TYPE_JSON_STRING"
internal const val KEY_NORMAL_SERIALIZATION_JSON_OBJECT_JSON_STRING = "KEY_NORMAL_SERIALIZATION_JSON_OBJECT_JSON_STRING"
internal const val KEY_NORMAL_SERIALIZATION_JSON_ARRAY_JSON_STRING = "KEY_NORMAL_SERIALIZATION_JSON_ARRAY_JSON_STRING"
internal const val KEY_CUSTOM_SERIALIZATION_JSON_STRING = "KEY_CUSTOM_SERIALIZATION_JSON_STRING"

/**
 * - If [condition] is `false` an exception will be thrown isa.
 */
internal fun checkTrue(condition: Boolean) {
    if (condition.not()) throw RuntimeException("False condition")
}

internal val objenesis by lazy {
    ObjenesisStd(true)
}
