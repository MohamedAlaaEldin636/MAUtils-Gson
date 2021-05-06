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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.maproductions.mohamedalaa.processor.custom_classes

import com.maproductions.mohamedalaa.processor.extensions.error
import com.maproductions.mohamedalaa.processor.extensions.warning
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

abstract class SpecialAbstractProcessor : AbstractProcessor() {

    protected val delegatePrintWriter = lazy {
        kotlin.runCatching {
            val file = File("${System.getProperty("user.dir")}\\gradle.properties")

            if (file.exists().not()) {
                processingEnv.error("File gradle.properties Not Found")
            }

            val keyOfGradleProperties = "file.to.write.to.in.processor="

            val lineAsString = file.readLines().first { it.startsWith(keyOfGradleProperties) }

            val startIndex = lineAsString.indexOf(keyOfGradleProperties)
            if (startIndex == -1) {
                processingEnv.error("File to use as a log cat in processor doesn't exist in gradle.properties isa.")
            }
            val path = lineAsString.substring(startIndex + keyOfGradleProperties.length).trim()

            PrintWriter(
                FileWriter(
                    File(path.substring(1, path.lastIndex)),
                    true
                )
            )
        }.getOrElse {
            processingEnv.warning("ERROR $it")

            null
        }
    }

    protected val printWriter by delegatePrintWriter

    final override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (annotations == null || roundEnv == null) return false

        return processAnnotation(annotations, roundEnv).also {
            if (delegatePrintWriter.isInitialized()) {
                printWriter?.flush()
                printWriter?.close()
            }
        }
    }

    protected abstract fun processAnnotation(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean

}
