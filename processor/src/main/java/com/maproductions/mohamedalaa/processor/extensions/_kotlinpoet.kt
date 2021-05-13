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

import com.maproductions.mohamedalaa.processor.ProcessorOfFullNames
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview

@KotlinPoetMetadataPreview
fun buildPropertySpecList(classesFullNames: Set<String>): PropertySpec {
    val itemType = KotlinpoetUtils.parameterizedTypeName(
        Class::class.asTypeName(),
        STAR,
    )

    val type = KotlinpoetUtils.parameterizedTypeName(
        List::class.asTypeName(),
        itemType,
    )

    val builder = PropertySpec.builder(ProcessorOfFullNames.propertyName, type).apply {
        // docs
        addKdoc(
            "- All classes targeted by the annotations isa." +
                    "\n\n" +
                    "- Shouldn't be used except by the library it's created in as there is no use of it outside that scope isa."
        )

        // Annotation
        addAnnotation(JvmField::class)

        // Code isa.
        initializer(
            "listOf(${classesFullNames.joinToString { it }})"
        )
    }

    return builder.build()
}

/**
 * @param fileSpecBuilder Needed to add import statements in it isa.
 */
@KotlinPoetMetadataPreview
fun buildFunctionSpecSetup(fileSpecBuilder: FileSpec.Builder, classesFullNames: Set<String>): FunSpec {
    fileSpecBuilder.addImport("com.google.gson", "Gson", "GsonBuilder")
    fileSpecBuilder.addImport("com.maproductions.mohamedalaa.core", "\$MA\$Gson", "fromJson")

    val paramUseDefaultGsonBuilderConfigs = ParameterSpec.builder("useDefaultGsonBuilderConfigs", Boolean::class)
        .defaultValue("true")
        .build()

    val paramCheckObjectDeclarationEvenIfNotAnnotated = ParameterSpec.builder("checkObjectDeclarationEvenIfNotAnnotated", Boolean::class)
        .defaultValue("false")
        .build()

    val typeOfParamGsonBuilderConfigs = LambdaTypeName.get(
        null,
        ClassName("com.google.gson", "GsonBuilder"),
        returnType = Unit::class.asTypeName()
    )

    val paramGsonBuilderConfigs = ParameterSpec.builder("gsonBuilderConfigs", typeOfParamGsonBuilderConfigs)
        .defaultValue("{}")
        .build()

    val builder = FunSpec.builder("setup").apply {
        // region docs
        addKdoc(
            "- **Must** be called on Application.onCreate to set up the library.\n" +
                    "\n" +
                    "- Not only sets up the library, But also provides customizations to the default used [Gson].\n" +
                    "\n" +
                    "- Note the library forces `GsonBuilder().disableHtmlEscaping()` as it needs it to work properly.\n" +
                    "\n" +
                    "@param checkObjectDeclarationEvenIfNotAnnotated If true then no need to annotate\n" +
                    "object declaration as it will be auto checked, But If true then on first time using\n" +
                    "[fromJson] it will take about 700 milli second to be invoked as it will use reflection.\n" +
                    "\n" +
                    "@param gsonBuilderConfigs in case you wanna make more customizations like\n" +
                    "[GsonBuilder.setFieldNamingStrategy] or any other customization to [GsonBuilder] isa.\n" +
                    "\n" +
                    "@param useDefaultGsonBuilderConfigs if `true` then default [Gson] instance that will be used\n" +
                    "(which can be retrieved by [getLibUsedGson]) will have the following code\n" +
                    "```\n" +
                    "GsonBuilder()\n" +
                    "     .serializeNulls()\n" +
                    "     .setLenient()\n" +
                    "     .enableComplexMapKeySerialization()\n" +
                    "```\n" +
                    "Otherwise just ```GsonBuilder()``` is used."
        )
        // endregion

        // Annotations
        addAnnotation(JvmStatic::class)
        addAnnotation(JvmOverloads::class)

        // Parameters
        addParameter(paramUseDefaultGsonBuilderConfigs)
        addParameter(paramCheckObjectDeclarationEvenIfNotAnnotated)
        addParameter(paramGsonBuilderConfigs)

        // Code isa.
        addStatement(
            "`\$MA\$Gson`.useDefaultGsonBuilderConfigs = useDefaultGsonBuilderConfigs" +
                    "\n" +
                    "`\$MA\$Gson`.checkObjectDeclarationEvenIfNotAnnotated = checkObjectDeclarationEvenIfNotAnnotated" +
                    "\n" +
                    "`\$MA\$Gson`.gsonBuilderConfigs = gsonBuilderConfigs" +
                    "\n" +
                    "`\$MA\$Gson`.allAnnotatedClasses = listOf(${classesFullNames.joinToString { it }})"
        )
    }

    return builder.build()
}

fun buildFunctionSpecGetLibUsedGson(fileSpecBuilder: FileSpec.Builder): FunSpec {
    fileSpecBuilder.addImport("com.google.gson", "Gson")
    fileSpecBuilder.addImport("com.maproductions.mohamedalaa.core", "toJson", "\$MA\$Gson")

    val builder = FunSpec.builder("getLibUsedGson").apply {
        // region docs
        addKdoc(
            "@return default [Gson] used by the library when you use [toJson] with no args isa."
        )
        // endregion

        // Annotations
        addAnnotation(JvmStatic::class)

        // Return Type
        returns(ClassName("com.google.gson", "Gson"))

        // Code
        addStatement("return `\$MA\$Gson`.getLibUsedGson()")
    }

    return builder.build()
}

fun FileSpec.Builder.addSuppressUnusedAnnotation() = apply {
    addAnnotation(
        AnnotationSpec.builder(Suppress::class)
            .addMember("\"unused\"")
            .build()
    )
}
