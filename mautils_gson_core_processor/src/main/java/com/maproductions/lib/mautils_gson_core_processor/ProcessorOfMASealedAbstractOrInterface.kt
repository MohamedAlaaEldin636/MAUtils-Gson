package com.maproductions.lib.mautils_gson_core_processor

import com.maproductions.lib.mautils_gson_core_annotation._AnnotationsConstants
import com.maproductions.lib.mautils_gson_core_processor.extensions.buildFunSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@SupportedAnnotationTypes("com.maproductions.lib.mautils_gson_core_annotation.MASealedAbstractOrInterface")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ProcessorOfMASealedAbstractOrInterface : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val mutableList = mutableListOf<String>()
        for (element in roundEnv.getElementsAnnotatedWith(_AnnotationsConstants.maSealedAbstractOrInterfaceJClass)) {
            if (element !is TypeElement) {
                continue
            }

            mutableList += element.qualifiedName.toString()
        }

        // todo 1. remove duplicates
        /*
        2. check other annotaation
        3. rename to processor bs isa. as it handles 2 annotations isa.
        4. remove unneeded deps here isa. see gradle isa javapoet and reflection isa.
         */

        // function
        val function = buildFunSpec(mutableList)

        // object
        val objectClass = TypeSpec.objectBuilder(_AnnotationsConstants.generatedMASealedAbstractOrInterfaceSimpleName)
            .addFunction(function)
            .build()

        // file
        val file = FileSpec.builder(
            _AnnotationsConstants.generatedMASealedAbstractOrInterfacePackageName,
            _AnnotationsConstants.generatedMASealedAbstractOrInterfaceSimpleName
        ).run {
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

}
