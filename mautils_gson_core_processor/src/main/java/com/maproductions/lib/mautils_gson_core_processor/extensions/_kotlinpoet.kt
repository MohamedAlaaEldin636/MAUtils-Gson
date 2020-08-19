package com.maproductions.lib.mautils_gson_core_processor.extensions

import com.squareup.kotlinpoet.*

fun buildFunSpec(classesFullNames: List<String>): FunSpec {
    val returnType = KotlinpoetUtils.parameterizedTypeName(
        List::class.asTypeName(),
        Class::class.asTypeName(),
        Any::class.asTypeName().copy(true)
    )

    val itemType = KotlinpoetUtils.parameterizedTypeName(
        Class::class.asTypeName(),
        Any::class.asTypeName().copy(true)
    )

    val builder = FunSpec.builder("getListOfClasses").apply {
        // Return Type isa.
        returns(returnType)

        // Code isa.
        addStatement(
            "val list = mutableListOf<%T>()", itemType
        )

        for (fullName in classesFullNames) {
            addStatement(
                """
                |kotlin.runCatching { Class.forName(%S) }.getOrNull()?.apply {
                |   list += this
                |}
                """.trimMargin(),
                fullName
            )
        }

        addStatement(
            "return list"
        )
    }

    return builder.build()
}
