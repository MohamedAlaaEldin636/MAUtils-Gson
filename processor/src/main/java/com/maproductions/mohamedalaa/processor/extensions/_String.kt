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

package com.maproductions.mohamedalaa.processor.extensions

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

fun String.replaceForwardSlashWithDot(): String = replace("/", ".")

fun String.toJavaWrapperOfPrimitiveOrNull(): String? {
    return "java.lang." + when (ClassName("", this)) {
        Int::class.asTypeName() -> "Integer"
        Float::class.asTypeName() -> "Float"
        Long::class.asTypeName() -> "Long"
        Double::class.asTypeName() -> "Double"
        Short::class.asTypeName() -> "Short"
        Byte::class.asTypeName() -> "Byte"
        Boolean::class.asTypeName() -> "Boolean"
        Char::class.asTypeName() -> "Character"
        else -> return null
    } + "::class.java"
}
