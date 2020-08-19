@file:Suppress("ClassName")

package com.maproductions.lib.mautilsgson.extensions

import com.maproductions.lib.flow_1.custom_classes.DataResult
import com.maproductions.lib.mautils_gson_core_annotation.MAProviderOfSealedAbstractOrInterface
import com.maproductions.lib.mautils_gson_core_annotation.MASealedAbstractOrInterface
import com.maproductions.mohamedalaa.core.model.UICountry

@MASealedAbstractOrInterface
@MAProviderOfSealedAbstractOrInterface(
    DataResult::class,
    UICountry::class
)
interface _ProviderOfSealedAbstractOrInterface

@MAProviderOfSealedAbstractOrInterface(String::class)
interface _I2
