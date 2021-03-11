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

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.asTypeName

fun buildFunSpec(classesFullNames: List<String>): FunSpec {
    val itemType = KotlinpoetUtils.parameterizedTypeName(
        Class::class.asTypeName(),
        STAR,
    )

    val returnType = KotlinpoetUtils.parameterizedTypeName(
        List::class.asTypeName(),
        itemType,
    )

    val builder = FunSpec.builder("getListOfClasses").apply {
        // Return Type isa.
        returns(returnType)

        // Code isa.
        addStatement(
            "return listOf(${classesFullNames.joinToString { "$it::class.java" }})"
        )
    }

    return builder.build()
}
