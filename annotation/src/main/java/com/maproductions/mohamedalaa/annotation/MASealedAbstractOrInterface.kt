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

package com.maproductions.mohamedalaa.annotation

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
 * that class can be serialized/deserialized correctly without any problem isa,
 * **Also** object class can be annotated so that on deserialization we keep the same instance isa,
 * to keep the Singleton pattern that object is meant to keep isa.
 *
 * - Don't forget to use the processor of this annotation in the same module
 * this annotation is used in, even if used in several modules (multi-module project) isa.
 *
 * @see MAProviderOfSealedAbstractOrInterface
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class MASealedAbstractOrInterface
