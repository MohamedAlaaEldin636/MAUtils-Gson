# MAUtils-Gson
[![](https://jitpack.io/v/MohamedAlaaEldin636/MAUtils-Gson.svg)](https://jitpack.io/#MohamedAlaaEldin636/MAUtils-Gson)
[![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![semantic-release](https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg)](https://github.com/semantic-release/semantic-release)

Android Library built on top of [Gson](https://github.com/google/gson) to provide awesome additional capabilities and utilities.

# VIP Temporary Note

- As of Version `3.0.0` & above several changes have occurred, But due to being busy there is currently no time to Update README to reflect these changes,
So please **Note** that below info in this README is only for Versions `2.0.2` & below, **And** asap I will update this README to include & explain new changes.

# Contents [▴](#mautils-gson)

- [Install](#install-)
    - [Jitpack Environment](#jitpack-environment-)
    - [Library](#library-)
        - [Case of using `core` module only](#case-of-using-core-module-only-)
        - [Case of using `core` & `annotation` & `processor` modules](#case-of-using-core--annotation--processor-modules-)
        
- [Features](#features-)
    - [`core` module only](#core-module-only-)
    - [`core` & `annotation` & `processor` modules](#core--annotation--processor-modules-)

- [Usage Examples](#usage-examples-)
    - [Nested Type Parameters](#nested-type-parameters-)
    - [(Kotlin) Object Support](#kotlin-object-support-)
    - [Sealed/Abstract/Interface Support](#sealedabstractinterface-support-)

- [Limitations](#limitations-)

- [Additional Notes](#additional-notes-)

- [License](#license-)

## Install [▴](#contents-)

- [Jitpack Environment](#jitpack-environment-)
- [Library](#library-)
    - [Case of using `core` module only](#case-of-using-core-module-only-)
    - [Case of using `core` & `annotation` & `processor` modules](#case-of-using-core--annotation--processor-modules-)

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

- X.Y.Z denotes app version which is [![](https://jitpack.io/v/MohamedAlaaEldin636/MAUtils-Gson.svg)](https://jitpack.io/#MohamedAlaaEldin636/MAUtils-Gson)

- [Case of using `core` module only](#case-of-using-core-module-only-)
- [Case of using `core` & `annotation` & `processor` modules](#case-of-using-core--annotation--processor-modules-)

#### Case of using `core` module only [▴](#library-)

- in your Gradle **Module-level** build file add below code

``` kotlin 
plugins {
    kotlin("android")
    // ...
}
// ...
dependencies {
    implementation("com.github.MohamedAlaaEldin636.MAUtils-Gson:core:X.Y.Z") {
        // Opt-out from annotation since it's only useful with processor.
        exclude("com.github.MohamedAlaaEldin636.MAUtils-Gson", "annotation")
    }
    // ...
}
```

#### Case of using `core` & `annotation` & `processor` modules [▴](#library-)

- **Note** You can add `core` module to all modules you need in your Project, **But**
`processor` module **Must** be added in your **app** module only this is a [limitation](#limitations-) of the library.

- in your Gradle **Module-level** build file add below code

``` kotlin 
plugins {
    kotlin("android")
    kotlin("kapt")
    // ...
}
// ...
dependencies {
    implementation("com.github.MohamedAlaaEldin636.MAUtils-Gson:core:X.Y.Z")
    
    // REMEMBER below processor MUST only be added in app module.
    kapt("com.github.MohamedAlaaEldin636.MAUtils-Gson:processor:X.Y.Z")
    // ...
}
```

## Features [▴](#contents-)

- [`core` module only](#core-module-only-)
- [`core` & `annotation` & `processor` modules](#core--annotation--processor-modules-)

### `core` module only [▴](#features-)

1. support conversion to/from JSON just by using extension functions of `Any?.toJson` & `String?.fromJson`, instead of you generating `Gson` instance then perform conversion.

2. (kotlin) object keyword(class) conversion maintains the same instance, which isn't supported by `Gson`.

3. Supports conversions of types with type params even in case of nested type params no matter how deep nesting is, Without the use of `TypeToken` for kotlin consumer code
(this is **temporary** not supported for **java** consumer code, this is a **temporary** [limitation](#limitations-) of the library) isa.

4. support conversion for the above cases even if they are inside another class as a property, or even if inside another subclass of one the annotated classes, so supported nested conversion as well.

5. have an extension functions for `Bundle` & `Intent` to put values via `Bundle.putJson` for any type whether supported by `Bundle` or not because if
not supported a conversion to json will be made by `Any?.toJsonOrNull`, Also instead of specifying type in method name so single `Bundle.putJson`
is better than several `putString`, `putInt` etc.

6. Supports special classes conversions, Currently only `Uri` is supported via `toString` & `Uri.parse` to convert to/from `String` when using `toJson`/`fromJson` isa.

### `core` & `annotation` & `processor` modules [▴](#features-)

1. support conversion of **sealed classes** / **abstract classes** / **interfaces**
this is only where the annotation is needed and only for root type Not needed for subclasses, **Note** you have to use `processor` module for that to work, **Also Note** this is supported for kotlin consumer code only for now but in future it's planned to make processor support java as well.

2. Annotations Processor also Supports **sealed classes** / **abstract classes** / **interfaces** that the developer doesn't own (Ex. from 3rd party library) by using `@MAProviderOfSealedAbstractOrInterface`, You can think of this as `@Provides` in Dagger library (If you don't know dagger it's ok, this was just an example to explain the annotation, you can instead read the annotation's documentation).

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

- Requires `processor` module for below code to work isa.

``` kotlin
// Declaration of classes 
@MASealedAbstractOrInterface
sealed class DataResult<T> {
    data class Success<T>(val value: T) : DataResult<T>()
}
@MASealedAbstractOrInterface
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

- When using `processor` module it **Must** only be added in your **app** Module only (in it's gradle.build.kts file as shown via code [here](#case-of-using-core--core_processor-modules-))

- Any subclass of the annotated class should either have no-args constructor or at least 1 constructor where it's params same as some or all of it's properties, **However** This can be ignored unless a problem in conversion happened, then you might check that recommendation.

- **temporarily** No support for java consumer code, Just because I have a very limited time and this library is made to increase my own productivity of other projects,
However it's planned to be supported in the future ASAP, Once I have some spare time.

## Additional Notes [▴](#contents-)

- This library initially is a migration from my other library [MAUtils](https://github.com/MohamedAlaaEldin636/MAUtils), Migration happened since utilities to be in 1 library is very complex.

- What happened since migration is that before migration you could only use processor with classes of same module, and now it's ok to use classes from other modules whether they are in the same project that you work on OR from any other 3rd party library.

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
