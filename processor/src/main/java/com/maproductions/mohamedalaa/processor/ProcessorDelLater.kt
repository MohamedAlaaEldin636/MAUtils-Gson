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

import com.maproductions.mohamedalaa.annotation.DelLater
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.specs.toTypeSpec
import com.squareup.kotlinpoet.metadata.toImmutableKmClass
import kotlinx.metadata.KmClassifier
import java.io.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.FileObject
import javax.tools.StandardLocation


/*
todo
fun fact about processor if declared in app module but used in core module it doesn't get called
we ba3den ba2a isa ?!

ezan limitation is only use in app module and even ur own modules if have them use @MAProvider
to access them isa.
 */
@KotlinPoetMetadataPreview
@SupportedAnnotationTypes(
    "com.maproductions.mohamedalaa.annotation.DelLater",
)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ProcessorDelLater : AbstractProcessor() {

    /**
     * ## Steps
     *
     * - annotations and kapt can be in any module not just app isa.
     * - in case a module already created the kt file delete it after reading it's content
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
     * - use new API approach for @MAProvider to be a data class and variables
     * have classes that are needed
     *
     * - in core api on registering adapters
     * have 2 types 1 for sealed abstract and interface and 1 for others
     * where no special json conversion except when deserialize fails isa.
     *
     * - so kotlin.String will be converted safely even if added isa.
     */
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (annotations == null || roundEnv == null) {
            return false
        }

        val printWriter = PrintWriter(
            FileWriter(
                File("F:\\Programs\\Text Editors\\Atom\\Projects\\Programming\\Del Later\\Processor.txt"),
                true
            )
        )

        printWriter.println("Hi !")
        printWriter.flush()

        for (variableElement in roundEnv.getElementsAnnotatedWith(DelLater::class.java).filterIsInstance<VariableElement>()) {
            //variableElement.constantValue
                    /*
                    here it should be ElementVariable isa.
                     */

            (variableElement.enclosingElement as? TypeElement)?.toImmutableKmClass()?.also {
                val l = it.toMutable()

                //ClassInspectorUtil.KOTLIN_INTRINSIC_INTERFACES
                //ClassName.bestGuess("")
                /*val v = it.properties.first()
                v.returnType

                //PropertySpec.builder() typeName type kclass
                //kotlinx.metadata.KmTypes
                PropertySpec.builder(
                    "",
                    it.properties.first().returnType.toMutable()
                )

                val v = processingEnv.filer.getResource(
                    StandardLocation.SOURCE_OUTPUT,
                    "",
                    "test.txt"
                )
                v.delete()*/

                /*var jfo: FileObject =
                    processingEnv.filer.getResource(StandardLocation.SOURCE_OUTPUT, "", "test.txt")
                val msg: String = TUtils.JFOToString(jfo) // Reads FileObject as String
                jfo.openReader(true).forEachLine {  }
                jfo.openReader(true).readLines()
                *//*
                todo will get rid of both probs lazm at app module and use annotations in it isa

                1. annotations can be in any module but kapt must be with it isa.
                2. keep provider for 3rd party apps
                3. get file content from previous processor if exists delete it then
                convert to kotlin poet hya di el problem read it's data again
                to get classes full names to keep .distinct as needed
                4. write again same file as u deleted it
                5. bs kda bs shof ba2e el todos bta3et ignore primitives w string w charsequnec w kda isa.
                6. IF CAN'T CONVERT BACK file content to kotlin poet since code is not that hard
                i guess i caan get it by myself search for \" and ending \" etc.. isa.
                7. if will work adjust GitHub and ensure it works before publish isa.
                8. use data class with constructor args as the MAProvided ones and have annotation
                that may include self but default to false isa.
                9. in core module check all registered classes and if there is one that is assignable
                from another then remove it since that is only known at runtime isa, hard on compile time isa.
                10. or 4get num 9 but ensure that an example of it works perfectly nested of nested
                i guess it does works but prob was that String should be ignored isa.
                11. see if processor can read data from gradle.properties even with File() directly
                since BUildCnfig makes errors and i guess this can be done just small amount of search isa
                and get project current dir msln isa. maybe System.env.kaza isa.

                to get property type from properties first .type below or above after enclosing class .toTypeSpec() isa.
                use that for room and shared prefs as well isa.
                 *//*
                // https://github.com/gmazzo/gradle-buildconfig-plugin
                System.console().printf("")
                jfo.delete()*/

                //jfo = processingEnv.filer.createResource(StandardLocation.SOURCE_OUTPUT, "", "test.txt")
                /*TUtils.writeJFO(
                    jfo,
                    msg + "Hallo ich bin Processor 2"
                )*/ // Writes String to FileObject

                listOf<Any?>(
                    "NEW LOOK -> " + it.name + " ||| " + it.name.replace("/", "."),
                    it.properties.first(),
                    it.properties.first().name,
                    it.properties.first().returnType.classifier,
                    (it.properties.first().returnType.classifier as? KmClassifier.Class)?.name,
                    it.properties.first().returnType,
                    it.properties.first().returnType.toMutable(),
                    it.properties.first().returnType.annotations,
                    it.properties.first().returnType,
                ).forEach { innerIt ->
                    printWriter.println(innerIt)
                }

            }

            printWriter.println(variableElement)
            printWriter.println(variableElement.simpleName)
            printWriter.println(variableElement.enclosingElement)

            printWriter.println((variableElement.enclosingElement as? TypeElement)?.toImmutableKmClass())
            printWriter.println((variableElement.enclosingElement as? TypeElement)?.toTypeSpec()/*.propertySpecs*/)
            printWriter.println((variableElement.enclosingElement as? TypeElement)?.toTypeSpec()?.superclass/*.propertySpecs*/)
            printWriter.println("LOOOOOOOOOOOOOOOOK " + (variableElement.enclosingElement as? TypeElement)?.toTypeSpec()?.propertySpecs?.firstOrNull()?.type/*.propertySpecs*/)
            /*
            todo from metadata api u visit class then get property and then check property class
             ymken hwa da el 7al isa.
             */
            printWriter.println((variableElement.enclosingElement as? TypeElement)?.asClassName())
            printWriter.println((variableElement.enclosingElement as? TypeElement)?.asType())

            printWriter.println(".")
        }

        printWriter.flush()
        printWriter.close()

        return false
    }

}
