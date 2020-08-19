package com.maproductions.lib.mautils_gson_core_annotation

/**
 * - Shouldn't be used except by this and mautils_gson_core modules isa.
 */
@Suppress("ClassName")
object _AnnotationsConstants {

    private const val prefixOfAllAnnotations = "_Generated"

    val maSealedAbstractOrInterfaceJClass = MASealedAbstractOrInterface::class.java

    val generatedMASealedAbstractOrInterfacePackageName: String
            = maSealedAbstractOrInterfaceJClass.getPackage().name

    val generatedMASealedAbstractOrInterfaceSimpleName: String
            = prefixOfAllAnnotations + maSealedAbstractOrInterfaceJClass.simpleName

    val generatedMASealedAbstractOrInterfaceFullName: String
            = "$generatedMASealedAbstractOrInterfacePackageName.$generatedMASealedAbstractOrInterfaceSimpleName"

}
