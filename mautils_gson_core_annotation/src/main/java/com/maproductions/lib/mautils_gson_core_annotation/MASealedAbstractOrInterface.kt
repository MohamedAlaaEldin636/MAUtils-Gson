package com.maproductions.lib.mautils_gson_core_annotation

/**
 * - Annotate Sealed Class, Abstract Class or Interface, And after that they can be converted via
 * `toJson`/`fromJson` functions isa, Checkout the below example.
 *
 * ``` kotlin
 * // Declaration
 * @MASealedAbstractOrInterface
 * abstract class AbstractClass
 *
 * data class Impl(var int: Int) : AbstractClass()
 *
 * // Conversion
 * val abstractClass: AbstractClass = Impl(33)
 * val json = abstractClass.toJson()
 * val value = json.fromJson<AbstractClass>()
 * assertEquals(abstractClass, value) // Test passed
 * ```
 *
 * - Not only that but also if a field inside a class with AbstractClass as a type
 * that class can be serialized/deserialized correctly without any problem isa.
 *
 * ### Limitations
 * - You need to use processor of this annotation in the "app" module only isa.
 *
 * @see MAProviderOfSealedAbstractOrInterface
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class MASealedAbstractOrInterface
