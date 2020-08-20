plugins {
    id(Config.Plugins.androidApplication)

    kotlin(Config.Plugins.kotlinAndroid)

    kotlin(Config.Plugins.kotlinAndroidExtensions)

    kotlin(Config.Plugins.kotlinKapt)
}

android {
    compileSdkVersion(Config.Android.compileSdk)
    buildToolsVersion(Config.Android.builtTools)

    defaultConfig {
        applicationId = Config.Android.applicationId

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
    // Project Modules
    implementation(project(Config.ProjectModules.feature_flow_1))

    implementation(project(Config.ProjectModules.mautils_gson_core))
    implementation(project(Config.ProjectModules.mautils_gson_core_annotation))
    kapt(project(Config.ProjectModules.mautils_gson_core_processor))

    testImplementation(project(Config.ProjectModules.core_1, "integrateTestImplementations"))
    androidTestImplementation(project(Config.ProjectModules.core_1, "integrateAndroidTestImplementations"))
}