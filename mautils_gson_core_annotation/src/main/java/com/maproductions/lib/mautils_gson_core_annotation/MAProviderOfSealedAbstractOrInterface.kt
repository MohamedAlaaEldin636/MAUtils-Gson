package com.maproductions.lib.mautils_gson_core_annotation

import kotlin.reflect.KClass

/**
 * - Must be annotated on an **interface** isa.
 *
 * - Has same effect as [MASealedAbstractOrInterface], **But** used with classes that doesn't exist
 * in the module that you used "kapt" (this library) in it, so when you use other 3rd party library
 * or even another library module in the same project add the needed classes in this annotation isa,
 * instead of [MASealedAbstractOrInterface] since you don't own them isa.
 *
 * - Note annotated class with this annotation will be ignored, yet will have access so after
 * compilation the unused lint warning won't appear isa, However you still can annotate it
 * with [MASealedAbstractOrInterface] in case you want it to be annotated with it isa.
 *
 * @param value shouldn't be empty, and if empty processor will show a warning indicating it is
 * redundant isa.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class MAProviderOfSealedAbstractOrInterface(
    vararg val value: KClass<*>,
)
