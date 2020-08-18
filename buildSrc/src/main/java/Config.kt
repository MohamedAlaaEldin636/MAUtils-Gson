import org.gradle.kotlin.dsl.PluginDependenciesSpecScope

object Config {

    object Android {
        const val applicationId = "com.maproductions.lib.mautilsgson"

        const val compileSdk = 29
        const val builtTools = "29.0.3"

        const val targetSdk = 29
        const val minSdk = 16

        const val versionCode = 1
        const val versionName = "1.0.0"

        const val androidJUnitRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val consumerRulesPro = "consumer-rules.pro"
    }

    /**
     * - Any property with a name prefix of kotlin should use [PluginDependenciesSpecScope].kotlin
     * extension fun [org.gradle.kotlin.dsl.kotlin] not [PluginDependenciesSpecScope.id] isa.
     */
    object Plugins {

        const val androidApplication = "com.android.application"

        const val androidLibrary = "com.android.library"

        const val kotlinAndroid = "android"

        const val kotlinAndroidExtensions = "android.extensions"

        const val kotlinKapt = "kapt"

    }

    object BuildPlugins {
        const val androidGradle = "com.android.tools.build:gradle:${Versions.gradle}"

        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }

    object ProjectModules {
        const val core_1 = ":core_1"
        const val core_2 = ":core_2"
        const val feature_flow_1 = ":feature:flow_1"
    }

    object Libs {
        const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

        const val app_compat = "androidx.appcompat:appcompat:${Versions.app_compat}"

        const val constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
        
        const val material_components = "com.google.android.material:material:${Versions.material_components}"

        const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    }

    object TestLibs {
        const val kotlin_test = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"

        const val junit = "junit:junit:${Versions.test_junit}"

        const val androidx_core = "androidx.test:core:${Versions.test_androidx_core}"

        // Robolectric environment -> @RunWith(RobolectricTestRunner::class)
        const val robolectric = "org.robolectric:robolectric:${Versions.test_robolectric}"

        const val androidx_runner = "androidx.test:runner:${Versions.test_androidx_core}"
        const val androidx_rules = "androidx.test:rules:${Versions.test_androidx_core}"

        const val androidx_junit = "androidx.test.ext:junit:${Versions.test_androidx_junit}"
        const val androidx_espresso = "androidx.test.espresso:espresso-core:${Versions.test_androidx_espresso}"
    }

    object Versions {
        const val kotlin = "1.4.0"
        const val gradle = "4.0.1"

        // Jetpack
        const val app_compat = "1.2.0"

        // Androidx
        const val constraint_layout = "2.0.0-rc1"

        // -- Others -- //

        const val material_components = "1.2.0"

        const val timber = "4.7.1"

        // Testing
        const val test_junit = "4.12"
        const val test_androidx_core = "1.2.0"
        const val test_androidx_junit = "1.1.1"
        const val test_androidx_espresso = "3.2.0"
        const val test_robolectric = "4.2.1"
    }

}