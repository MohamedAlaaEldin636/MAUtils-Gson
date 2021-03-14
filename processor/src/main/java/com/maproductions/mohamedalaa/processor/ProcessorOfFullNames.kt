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
import com.maproductions.mohamedalaa.processor.extensions.*
import com.squareup.kotlinpoet.*
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

/**
 * - **aggregating** incremental annotation processor isa.
 *
 * - **User** of this processor must include `-parameters` compiler argument isa.
 *
 * - Creates a single fun in an object in a file that returns a [List] of the full names of all
 * annotated types by [MASealedAbstractOrInterface] and all classes declared as params in
 * all [MAProviderOfSealedAbstractOrInterface] annotations isa.
 *
 * ## Steps
 *
 * - Annotations and kapt can be in any module not just app isa,
 * even if can't delete but check existence isa, make special pattern ex. sameName1, 2, 3 etc...
 * and get it dynamically if exists isa.
 *
 * - In case a module already created the kt file delete it after reading it's content
 * then create a new one code required for that is
 * ```
 * val v = processingEnv.filer.getResource(
 * StandardLocation.SOURCE_OUTPUT,
 * "", // package
 * "test.txt" // simple name .kt
 * )
 * v.delete()
 *
 * var jfo: FileObject =
 * processingEnv.filer.getResource(StandardLocation.SOURCE_OUTPUT, "", "test.txt")
 * val msg: String = TUtils.JFOToString(jfo) // Reads FileObject as String
 * jfo.openReader(true).forEachLine {  }
 * jfo.openReader(true).readLines()
 * // or .use\lines for auto .close
 * // then create a new one via the kotlinpoet API
 * ```
 *
 * - Use metadata for both of the APIs isa. @Metadata
 *
 * - Use new API approach for @MAProvider to be a data class and variables
 * have classes that are needed
 *
 * - In core api on registering adapters
 * have 2 types 1 for sealed abstract and interface and 1 for others
 * where no special json conversion except when deserialize fails isa.
 *
 * - so kotlin.String will be converted safely even if added isa.
 */
@SupportedAnnotationTypes(
    "com.maproductions.mohamedalaa.annotation.MASealedAbstractOrInterface",
    "com.maproductions.mohamedalaa.annotation.MAProviderOfSealedAbstractOrInterface",
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
            var maProviderOfSealedAbstractOrInterface = MAProviderOfSealedAbstractOrInterface::class.toString()
            maProviderOfSealedAbstractOrInterface = maProviderOfSealedAbstractOrInterface.substring(maProviderOfSealedAbstractOrInterface.indexOf(" ").inc())

            val annotationMirror = element.annotationMirrors.firstOrNull {
                it.annotationType.toString() == maProviderOfSealedAbstractOrInterface
            } ?: continue

            var fullNames /* Array of fullNames */ = annotationMirror.elementValues.entries
                .firstOrNull()?.value?.toString() ?: continue

            fullNames = fullNames.replace("{", "")
            fullNames = fullNames.replace("}", "")
            fullNames = fullNames.replace(" ", "")

            fullNamesList = fullNamesList + fullNames.split(",").map {
                val index = it.lastIndexOf(".class")

                it.substring(0, index)
            }
        }

        if (fullNamesList.isEmpty()) {
            return false
        }

        // Exclude special classes isa.
        fullNamesList = fullNamesList - listOf(
            "android.net.Uri"
        )

        // function
        val function = buildFunSpec(fullNamesList.distinct())

        // object
        val objectClass = TypeSpec.objectBuilder(_AnnotationsConstants.generatedMASealedAbstractOrInterfaceSimpleName)
            .addFunction(function)
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
            "com.maproductions.mohamedalaa.annotation.MASealedAbstractOrInterface",
            "com.maproductions.mohamedalaa.annotation.MAProviderOfSealedAbstractOrInterface",
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }

}
