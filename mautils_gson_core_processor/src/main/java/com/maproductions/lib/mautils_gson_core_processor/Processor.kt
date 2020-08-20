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

package com.maproductions.lib.mautils_gson_core_processor

import com.maproductions.lib.mautils_gson_core_annotation.MAProviderOfSealedAbstractOrInterface
import com.maproductions.lib.mautils_gson_core_annotation._AnnotationsConstants
import com.maproductions.lib.mautils_gson_core_processor.extensions.buildFunSpec
import com.maproductions.lib.mautils_gson_core_processor.extensions.warning
import com.squareup.kotlinpoet.*
import java.io.IOException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror

@SupportedAnnotationTypes(
    "com.maproductions.lib.mautils_gson_core_annotation.MASealedAbstractOrInterface",
    "com.maproductions.lib.mautils_gson_core_annotation.MAProviderOfSealedAbstractOrInterface",
)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class Processor : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val mutableList = mutableListOf<String>()

        val toBeAccessedFullNames = mutableListOf<String>()

        // MASealedAbstractOrInterface
        for (element in roundEnv.getElementsAnnotatedWith(_AnnotationsConstants.maSealedAbstractOrInterfaceJClass)) {
            if (element !is TypeElement) {
                continue
            }

            mutableList += element.qualifiedName.toString()
        }

        // MAProviderOfSealedAbstractOrInterface
        for (element in roundEnv.getElementsAnnotatedWith(MAProviderOfSealedAbstractOrInterface::class.java)) {
            if (element !is TypeElement) {
                continue
            }

            for (provider in element.getAnnotationsByType(MAProviderOfSealedAbstractOrInterface::class.java)) {
                val typeMirrors = try {
                    @Suppress("UNCHECKED_CAST")
                    provider.value.toList() as List<TypeMirror>
                }catch (e: MirroredTypesException) {
                    e.typeMirrors.orEmpty()
                }

                if (typeMirrors.isEmpty()) {
                    processingEnv.warning(
                        "@MAProviderOfSealedAbstractOrInterface Used with empty list which " +
                                "is 'redundant', (Usage was with \"${element.qualifiedName}\")"
                    )

                    continue
                }

                toBeAccessedFullNames += element.qualifiedName.toString()

                mutableList += typeMirrors.map { it.toString() }
            }
        }

        // Check not empty isa.
        if (mutableList.isEmpty()) {
            return false
        }

        // function
        val function = buildFunSpec(mutableList.distinct())

        // object
        val objectClass = TypeSpec.objectBuilder(_AnnotationsConstants.generatedMASealedAbstractOrInterfaceSimpleName)
            .addFunction(function)
            .build()

        // Accessing provider annotated classes to remove unused lint check on it isa.
        val property = PropertySpec.builder("access", String::class.asTypeName(), KModifier.PRIVATE)
            .initializer(
                toBeAccessedFullNames.joinToString(" + ") { "$it::class.java.name" }
            )
            .build()

        // file
        val file = FileSpec.builder(
            _AnnotationsConstants.generatedMASealedAbstractOrInterfacePackageName,
            _AnnotationsConstants.generatedMASealedAbstractOrInterfaceSimpleName
        ).run {
            addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .addMember("\"unused\"")
                    .build()
            )

            addType(objectClass)

            addProperty(property)

            build()
        }

        try {
            file.writeTo(processingEnv.filer)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

}
