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

import kotlin.reflect.KClass

/**
 * - Has same effect as [MASealedAbstractOrInterface], **But** used with classes that doesn't exist
 * in the module that you used "kapt" (this library) in it, so when you use other 3rd party library
 * or even another library module in the same project add the needed classes in this annotation isa,
 * instead of [MASealedAbstractOrInterface] since you don't own them isa.
 *
 * - Note annotated class with this annotation will be ignored, yet it will be accessed so after
 * compilation the unused lint warning won't appear isa, However you still can annotate it
 * with [MASealedAbstractOrInterface] in case you want it to be annotated with it isa.
 *
 * @param value shouldn't be empty, Otherwise it's useless isa.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class MAProviderOfSealedAbstractOrInterface(
    vararg val value: KClass<*>,
)
