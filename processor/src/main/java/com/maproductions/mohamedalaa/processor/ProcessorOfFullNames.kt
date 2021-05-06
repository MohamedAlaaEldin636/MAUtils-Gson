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

import com.maproductions.mohamedalaa.annotation.MAProviderOfAbstracts
import com.maproductions.mohamedalaa.annotation.MAAbstract
import com.maproductions.mohamedalaa.processor.custom_classes.SpecialAbstractProcessor
import com.maproductions.mohamedalaa.processor.extensions.*
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.toImmutableKmClass
import kotlinx.metadata.KmClassifier
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import java.io.IOException
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement


/**
 * - Creates a single fun in an object in a file that returns a [List] of the full names of all
 * annotated types by [MAAbstract] and all classes declared as params in
 * all [MAProviderOfAbstracts] annotations isa.
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
 *
 * todo delete kapt generated file and read contents and then regenerate with same + new contents isa.
 */
@KotlinPoetMetadataPreview
@SupportedAnnotationTypes(
    "com.maproductions.mohamedalaa.annotation.MAAbstract",
    "com.maproductions.mohamedalaa.annotation.MAProviderOfAbstracts",
)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ProcessorOfFullNames : SpecialAbstractProcessor() {

    // todo finish all above steps isa, deletions and 2 types isa.

    /*
    since limitation will need 2 things annotate app module application class
    +
    call on create setup

    todo then generated _ classes have just list and MAGson doesn't have it and it is inside setup isa.
     */

    companion object {
        private const val generatedClassSimpleName = "MAGson"

        const val propertyName = "list"
    }

    private val specialClasses = listOf(
        "android.net.Uri"
    )

    private val generatedClassPackageName by lazy {
        MAAbstract::class.java.`package`.name + ".generated_as_internal_helper_package"
    }

    override fun processAnnotation(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        // todo then if _GonConverter can be replaced by MATypes if not due to ? super wildcard to normal
        // then make in MATypes wildcard.eliminateWildcards isa. and check if works isa and del _GsonConverter
        // to remove duplicate code isa.

        /*
        Cases

        todo for sake of all cases
        1,2 app -> SUCCESS el7
        1,2 3 app
        1 2,3 app
        1 2 3 app but no annotations in 2 kda isa.
        1 2 app
        where , means app depends on both and both depend on not each other isa.

        todo then after processor is 100% isa, correct then adjust _Gson to make allANnotat... val
        be removed and use the one in $MA$Gson which will be planted and in tests since App not called
        nta b nafsak call MAGson.setup isa.
         */
        val classes = kotlin.runCatching {
            val reflections = Reflections(
                generatedClassPackageName,
                SubTypesScanner(false),
            )

            reflections.getSubTypesOf(Any::class.java)
        }.getOrNull().orEmpty()

        var fullNamesList = emptyList<String>()
        fullNamesList = fullNamesList + kotlin.runCatching {
            classes.mapNotNull { clazz ->
                val field = clazz.fields.firstOrNull { it.name == propertyName }
                    ?: return@mapNotNull null
                field.isAccessible = true
                @Suppress("UNCHECKED_CAST")
                val list = field.get(null) as List<Class<*>>
                list.mapNotNull {
                    if (it.isPrimitive) it.kotlin.javaObjectType.kotlin.qualifiedName else it.name
                }
            }.flatten()
        }.getOrNull().orEmpty()

        fullNamesList = fullNamesList + roundEnv.getElementsAnnotatedWith(MAAbstract::class.java)
            .filterIsInstance<TypeElement>().map { it.toImmutableKmClass().name.replaceForwardSlashWithDot() }

        for (element in roundEnv.getElementsAnnotatedWith(MAProviderOfAbstracts::class.java).filterIsInstance<TypeElement>()) {
            fullNamesList = fullNamesList + element.toImmutableKmClass().properties.mapNotNull {
                (it.returnType.classifier as? KmClassifier.Class)?.name?.replaceForwardSlashWithDot()
            }
        }

        if (fullNamesList.isEmpty()) {
            return false
        }

        // Exclude special classes isa.
        fullNamesList = fullNamesList - specialClasses

        // File Builder & Property & Functions
        val helperClassSimpleName = "_${System.nanoTime()}"
        val publicClassSimpleName = generatedClassSimpleName

        val helperClassFileSpecBuilder = FileSpec.builder(generatedClassPackageName, helperClassSimpleName)
        val publicClassFileSpecBuilder = FileSpec.builder(generatedClassPackageName, publicClassSimpleName)

        val propertySpecList = buildPropertySpecList(fullNamesList.distinct())
        val functionSpecSetup = buildFunctionSpecSetup(helperClassFileSpecBuilder, fullNamesList.distinct())
        val functionSpecGetLibUsedGson = buildFunctionSpecGetLibUsedGson(helperClassFileSpecBuilder)

        for (import in helperClassFileSpecBuilder.imports) {
            publicClassFileSpecBuilder.addImport(import)
        }

        // objects
        val helperClassObjectClass = TypeSpec.objectBuilder(helperClassSimpleName).apply {
            addProperty(propertySpecList)
        }.build()
        val publicClassObjectClass = TypeSpec.objectBuilder(publicClassSimpleName).apply {
            addFunction(functionSpecSetup)

            addFunction(functionSpecGetLibUsedGson)
        }.build()

        // files
        val helperClassFile = helperClassFileSpecBuilder
            .addSuppressUnusedAnnotation()
            .addType(helperClassObjectClass)
            .build()
        val publicClassFile = publicClassFileSpecBuilder
            .addSuppressUnusedAnnotation()
            .addType(publicClassObjectClass)
            .build()

        try {
            helperClassFile.writeTo(processingEnv.filer)
            publicClassFile.writeTo(processingEnv.filer)
        }catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            "com.maproductions.mohamedalaa.annotation.MAAbstract",
            "com.maproductions.mohamedalaa.annotation.MAProviderOfAbstracts",
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }

}
