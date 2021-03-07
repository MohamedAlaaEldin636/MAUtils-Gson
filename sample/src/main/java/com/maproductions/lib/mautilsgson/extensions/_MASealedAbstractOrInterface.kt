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

@file:Suppress("ClassName")

package com.maproductions.lib.mautilsgson.extensions

import com.maproductions.lib.flow_1.custom_classes.DataResult
import com.maproductions.lib.mautils_gson_core_annotation.MAProviderOfSealedAbstractOrInterface
import com.maproductions.lib.mautils_gson_core_annotation.MASealedAbstractOrInterface
import com.maproductions.mohamedalaa.core.model.UICountry

//@MASealedAbstractOrInterface // Can be used, but not needed in the example.
@MAProviderOfSealedAbstractOrInterface(
    DataResult::class,
    UICountry::class,
    //DataResult.Error::class, // Not needed at all.
)
interface _ProviderOfSealedAbstractOrInterface
