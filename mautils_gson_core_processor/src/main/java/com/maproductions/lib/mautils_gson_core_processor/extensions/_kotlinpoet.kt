package com.maproductions.lib.mautils_gson_core_processor.extensions

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
