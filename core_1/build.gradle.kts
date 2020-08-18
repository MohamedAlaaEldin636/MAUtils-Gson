plugins {
    id(Config.Plugins.androidLibrary)

    kotlin(Config.Plugins.kotlinAndroid)

    kotlin(Config.Plugins.kotlinAndroidExtensions)

    kotlin(Config.Plugins.kotlinKapt)
}

android {
    compileSdkVersion(Config.Android.compileSdk)
    buildToolsVersion(Config.Android.builtTools)

    defaultConfig {
        minSdkVersion(Config.Android.minSdk)
        targetSdkVersion(Config.Android.targetSdk)

        versionCode = Config.Android.versionCode
        versionName = Config.Android.versionName

        // Used to use VectorDrawableCompat in XML isa.
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = Config.Android.androidJUnitRunner
        consumerProguardFiles(Config.Android.consumerRulesPro)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(Config.Libs.kotlin_stdlib)

    api(Config.Libs.app_compat)

    api(Config.Libs.constraint_layout)

    api(Config.Libs.material_components)

    api(Config.Libs.timber)

    // -- Unit Testing -- //

    testImplementation(Config.TestLibs.junit)

    testImplementation(Config.TestLibs.kotlin_test)

    testImplementation(Config.TestLibs.androidx_core)

    testImplementation(Config.TestLibs.robolectric)

    // -- Instrumental Testing -- //

    androidTestImplementation(Config.TestLibs.androidx_runner)
    androidTestImplementation(Config.TestLibs.androidx_rules)

    androidTestImplementation(Config.TestLibs.androidx_junit)
    androidTestImplementation(Config.TestLibs.androidx_espresso)
}

val integrateTestImplementations: Configuration by configurations.creating {
    extendsFrom(configurations["testImplementation"])
}
val integrateAndroidTestImplementations: Configuration by configurations.creating {
    extendsFrom(configurations["androidTestImplementation"])
}

