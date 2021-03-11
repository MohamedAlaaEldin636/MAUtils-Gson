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

package com.maproductions.mohamedalaa.processor

import com.maproductions.mohamedalaa.annotation.MAProviderOfSealedAbstractOrInterface
import com.maproductions.mohamedalaa.annotation.MASealedAbstractOrInterface
import com.maproductions.mohamedalaa.annotation._AnnotationsConstants
import com.maproductions.mohamedalaa.processor.extensions.buildFunSpec
import com.maproductions.mohamedalaa.processor.extensions.note
import com.maproductions.mohamedalaa.processor.extensions.noteSafely
import com.maproductions.mohamedalaa.processor.extensions.warning
import com.squareup.kotlinpoet.*
import kotlinx.metadata.*
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import kotlinx.metadata.jvm.getterSignature
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationValueVisitor
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror

/**
 * - **aggregating** incremental annotation processor isa.
 *
 * - **User** of this processor must include `-parameters` compiler argument isa.
 *
 * - Creates a single fun in an object in a file that returns a [List] of the full names of all
 * annotated types by [MASealedAbstractOrInterface] and all classes declared as params in
 * all [MAProviderOfSealedAbstractOrInterface] annotations isa.
 *
 * - todo try instead of type mirrors to get via metadata if possible kda isa.
 */
@SupportedAnnotationTypes(
    "com.maproductions.lib.mautils_gson_core_annotation.MASealedAbstractOrInterface",
    "com.maproductions.lib.mautils_gson_core_annotation.MAProviderOfSealedAbstractOrInterface",
)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ProcessorOfFullNames : AbstractProcessor() {

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (annotations == null || roundEnv == null) {
            return false
        }

        var fullNamesList = roundEnv.getElementsAnnotatedWith(MASealedAbstractOrInterface::class.java)
            .filterIsInstance<TypeElement>().map { it.qualifiedName.toString() }

        for (element in roundEnv.getElementsAnnotatedWith(MAProviderOfSealedAbstractOrInterface::class.java).filterIsInstance<TypeElement>()) {
            val annotation = element.getAnnotation(MAProviderOfSealedAbstractOrInterface::class.java)
                ?: continue

            val typeMirrors = try {
                @Suppress("UNCHECKED_CAST")
                annotation.value.toList() as List<TypeMirror>
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

            fullNamesList = fullNamesList + typeMirrors.map { it.toString() }

            var counter = 0
            processingEnv.noteSafely(element.annotationMirrors) { processingEnv.note(counter++) }
            processingEnv.noteSafely(element.annotationMirrors) { processingEnv.note(counter++) }
            processingEnv.noteSafely(element.annotationMirrors.firstOrNull()) { processingEnv.note(counter++) }
            processingEnv.noteSafely(element.annotationMirrors.firstOrNull()?.annotationType) { processingEnv.note(counter++) }
            processingEnv.noteSafely(element.annotationMirrors.firstOrNull()?.elementValues) { processingEnv.note(counter++) }
            processingEnv.noteSafely(element.annotationMirrors.firstOrNull()?.elementValues?.entries?.firstOrNull()) { processingEnv.note(counter++) }
            processingEnv.noteSafely(element.annotationMirrors.firstOrNull()?.elementValues?.entries?.firstOrNull()?.key) { processingEnv.note(counter++) }
            processingEnv.noteSafely(element.annotationMirrors.firstOrNull()?.elementValues?.entries?.firstOrNull()?.value) { processingEnv.note(counter++) }
            /*element.annotationMirrors TODO S isa.
            element.annotationMirrors.first().annotationType
            element.annotationMirrors.first().elementValues
            val v = element.annotationMirrors.first().elementValues.entries.first()
            v.key
            v.value.value*/
        }

        if (fullNamesList.isEmpty()) {
            return false
        }

        // function
        val function = buildFunSpec(fullNamesList.distinct())

        // object
        val objectClass = TypeSpec.objectBuilder(_AnnotationsConstants.generatedMASealedAbstractOrInterfaceSimpleName)
            .addFunction(function)
            //.addOriginatingElement(originatingElement = roundEnv.getElementsAnnotatedWith(_AnnotationsConstants.maSealedAbstractOrInterfaceJClass).first())
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

            build()
        }

        try {
            file.writeTo(processingEnv.filer)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            "com.maproductions.lib.mautils_gson_core_annotation.MASealedAbstractOrInterface",
            "com.maproductions.lib.mautils_gson_core_annotation.MAProviderOfSealedAbstractOrInterface",
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }

}
