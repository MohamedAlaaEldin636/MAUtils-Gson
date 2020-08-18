package com.maproductions.mohamedalaa.core.model

/*
todo s
1. annotate with annotation so it can be used with .toJson()/.fromJson() isa.
2. try adding props in sealed class ex. sealed class UICountry(var isBookmarked: Boolean = false)
with 1 of the subclasses add true isa.
 */
sealed class UICountry {

    data class RelatedWithRegionsCountry(
        var isBookmarked: Boolean,

        var code: String,

        var name: String,

        var flagImageUri: String,

        var totalNumOfRegions: Int,

        var currencyCodes: List<String>
    ) : UICountry()

    data class SmallInfoCountry(
        var isBookmarked: Boolean,

        var alpha2Code: String,
        var alpha3Code: String,

        var name: String,

        var capital: String,

        var callingCodes: List<String>,

        var altSpellings: List<String>,

        var languages: List<String>,

        var currencies: List<String>
    ) : UICountry()

}
