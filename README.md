# MAUtils-Gson
TODO badge jitpack
[![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![semantic-release](https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg)](https://github.com/semantic-release/semantic-release)

Android Library built on top of [Gson](https://github.com/google/gson) to provide awesome additional capabilities and utilities.

# Contents [▴](#mautils-gson)

- [Install](#install-)
    - [Jitpack Environment](jitpack-environment-)
    - [Library](library-)
        - [Case of using `core` module only](#case-of-using-core-module-only-)
        - [Case of using `core` & `core_processor` modules](#case-of-using-core--core_processor-modules-)
        
- [Features](#features-)

- [Usage Examples](#usage-examples-)
    - [Nested Type Parameters](#nested-type-parameters-)
    - [(Kotlin) Object Support](#kotlin-object-support-)
    - [Sealed/Abstract/Interface Support](#sealedabstractinterface-support-)

- [Limitations](#limitations-)

- [Additional Notes](#additional-notes-)

- [License](#license-)

## Install [▴](#contents-)

- [Jitpack Environment](jitpack-environment-)
- [Library](library-)
    - [Case of using `core` module only](#case-of-using-core-module-only-)
    - [Case of using `core` & `core_processor` modules](#case-of-using-core--core_processor-modules-)

### Jitpack Environment [▴](#install-)

- in your Gradle **Top-level** build file add below code

``` kotlin
// Note this is Kotlin DSL Not Groovy DSL.
allprojects {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Library [▴](#install-)

- Note this library is divided into 3 modules, So it's either you wanna use the core functionality only or with the annotation processor as well.

- [Case of using `core` module only](#case-of-using-core-module-only-)
- [Case of using `core` & `core_processor` modules](#case-of-using-core--core_processor-modules-)

#### Case of using `core` module only [▴](#library-)

- in your Gradle **Module-level** build file add below code

``` kotlin 
plugins {
    kotlin("android")
    // ...
}
// ...
dependencies {
    implementation("TODO AFTER BEING DONE BY JITPACK CHECK HERE.")
    // ...
}
```

#### Case of using `core` & `core_processor` modules [▴](#library-)

- **Note** You can add `core` module to all modules you need in your Project, **But**
`core_processor` module **Must** be added in your **app** module only this is a [limitation](#limitations-) of the library.

- in your Gradle **Module-level** build file add below code

``` kotlin 
plugins {
    kotlin("android")
    kotlin("kapt")
    // ...
}
// ...
dependencies {
    implementation("TODO AFTER BEING DONE BY JITPACK CHECK HERE.")
    
    // REMEMBER below processor MUST only be added in app module.
    kapt("TODO AFTER BEING DONE BY JITPACK CHECK HERE.")
    // ...
}
```

## Features [▴](#contents-)

1. support conversion to/from JSON just by using extension functions of `Any?.toJson` & `String?.fromJson`, instead of you generating `Gson` instance then perform conversion.

2. (kotlin) object keyword(class) conversion maintains the same instance, which isn't supported by `Gson`.

3. support conversion of **sealed classes** / **abstract classes** / **interfaces**
this is only where the annotation is needed and only for root type Not needed for subclasses, **Note** you have to use `mautils_gson_core_processor` module for that to work, **Also Note** this is supported for kotlin consumer code only for now but in future it's planned to make processor support java as well.

4. Supports conversions of types with type params even in case of nested type params no matter how deep nesting is, Without the use of `TypeToken` for kotlin consumer code 
(for **java** consumer code `GsonConverter` is needed in case of any type params like `TypeToken`) isa.

5. support conversion for the above cases even if they are inside another class as a property, or even if inside another subclass of one the annotated classes, so supported nested conversion as well.

6. Annotations Prcessor Supports any classes that the developer doesn't own (Ex. from 3rd paty library) by using `@MAProviderOfSealedAbstractOrInterface`, You can think of this as `@Provides` in Dagger library (If you don't know dagger it's ok, this was just an example to explain the annotation, you can instead read the annotation's documentation).

## Usage Examples [▴](#contents-)

- [Nested Type Parameters](#nested-type-parameters-)
- [(Kotlin) Object Support](#kotlin-object-support-)
- [Sealed/Abstract/Interface Support](#sealedabstractinterface-support-)

### Nested Type Parameters [▴](#usage-examples-)

``` kotlin
// Convert custom object with nested type params just by using toJson/fromJson.
val map: Map<String, List<Pair<Int, Int>>> = mapOf("" to listOf(4 to 3, 5 to 3, 33 to 3))
val json = map.toJson() // No need for `TypeToken` like in `Gson`
val value = json.fromJson<Map<String, List<Pair<Int, Int>>>>()
assertEquals(map, value) // Passed
```

<details>
<summary><i><strong>Java Consumer Code</strong></i></summary>
<p>

``` java
// Custom Object conversion
String json = GsonUtils.toJson(customObject);
CustomObject value = GsonUtils.fromJson(json, CustomObject.class);
assertEquals(customObject, value);

// Type params need GsonConverter -> can be created anonymously or in a single file whatever you like.
Map<String, List<Pair<Integer, Integer>>> map = // ...
GsonConverter<Map<String, List<Pair<Integer, Integer>>>> gsonConverter = new GsonConverter<Map<String, List<Pair<Integer, Integer>>>>() {};
String json = gsonConverter.toJson(map);
Map<String, List<Pair<Integer, Integer>>> value = gsonConverter4.fromJson(json);
assertEquals(map, value);
```

</p>
</details>

### (Kotlin) Object Support [▴](#usage-examples-)

``` kotlin
val objectInstance = ObjectClass // creation code -> `object ObjectClass`
val json = objectInstance.toJson()
val value = json.fromJson<ObjectClass>()
assertEquals(objectInstance, value) // Passed
```

### Sealed/Abstract/Interface Support [▴](#usage-examples-)

``` kotlin
// Declaration of classes 
sealed class DataResult<T> {
    data class Success<T>(val value: T) : DataResult<T>()
}
sealed class UICountry(var isBookmarked: Boolean) {
    data class SmallInfoCountry(
        var name: String,
        var currencies: List<String>
    ) : UICountry(true)
}

// Conversion
val dataResult: DataResult<UICountry> = DataResult.Success(
    UICountry.SmallInfoCountry(
        "Name",
        listOf("C1", "C2", "C3")
    )
)
val json = dataResult.toJson()
val value = json.fromJson<DataResult<UICountry>>()
assertEquals(dataResult, value) // Passed
```

## Limitations [▴](#contents-)

- When using `mautils_gson_core_processor` module it **Must** only be added in your **app** Module only (in it's gradle.build.kts file as shown via code [here](#case-of-using-core--core_processor-modules-))

- This is not a limitation but recommendation, Any subclass of the annotated class should either have no-args constructor or at least 1 constructor where it's params same as some or all of it's properties, **However** This can be ignored unless a problem in conversion happened, then you might check that recommendation.

## Additional Notes [▴](#contents-)

- This library initially is a migration from my other library [MAUtils](https://github.com/MohamedAlaaEldin636/MAUtils), Migration happened since utilities to be in 1 library is very complex.

- What happpned since migration is that before migration you could only use processor with classes of same module, and now it's ok to use classes from other modules whether they are in the same project that you work on OR from any other 3rd paty library.

## [License](https://github.com/MohamedAlaaEldin636/MAUtils-Gson/blob/master/LICENSE) [▴](#contents-)

```
Copyright © 2020 Mohamed Alaa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
```

| Permissions         | Limitations           | Conditions   |
| ------------------- | --------------------- | ----------- |
| :heavy_check_mark: Commercial Use | :x: Trademark use | :information_source: License and copyright notice |
| :heavy_check_mark: Modification | :x: Liability | :information_source: State changes |
| :heavy_check_mark: Distribution | :x: Warranty | - |
| :heavy_check_mark: Patent use | - | - |
| :heavy_check_mark: Private use | - | - |
