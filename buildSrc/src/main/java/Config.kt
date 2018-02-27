@file:Suppress("MayBeConstant", "unused")

private const val androidGradleVersion = "3.0.1"
private const val kotlinVersion = "1.2.21"

// Compile dependencies
private const val architectureComponentsVersion = "1.1.0"
private const val constraintLayoutVersion = "1.0.2"
private const val koinVersion = "0.8.2"
private const val roomVersion = "1.0.0"
private const val rxBindingVersion = "2.1.1"
private const val rxJavaVersion = "2.1.10"
private const val rxRelayVersion = "2.0.0"
private const val supportVersion = "27.0.2"

// Test dependencies
private const val jUnitVersion = "4.12"
private const val espressoVersion = "3.0.1"

object Config {

    object BuildPlugins {
        val androidGradle = "com.android.tools.build:gradle:$androidGradleVersion"
        val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    }

    object Android {
        val buildToolsVersion = "27.0.3"
        val minSdkVersion = 15
        val targetSdkVersion = 27
        val compileSdkVersion = 27
        val applicationId = "xyz.thomasmohr.onfire"
        val versionCode = 1
        val versionName = "1.0"
    }

    object Libs {
        val kotlin_std = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"

        val appcompat = "com.android.support:appcompat-v7:$supportVersion"
        val cardview = "com.android.support:cardview-v7:$supportVersion"
        val constraintLayout =
            "com.android.support.constraint:constraint-layout:$constraintLayoutVersion"
        val design = "com.android.support:design:$supportVersion"
        val recyclerview = "com.android.support:recyclerview-v7:$supportVersion"

        val lifecycleExtensions = "android.arch.lifecycle:extensions:$architectureComponentsVersion"
        val roomRuntime = "android.arch.persistence.room:runtime:$roomVersion"
        val roomRxJava = "android.arch.persistence.room:rxjava2:$roomVersion"
        val roomCompiler = "android.arch.persistence.room:compiler:$roomVersion"
        val lifecycleCompiler = "android.arch.lifecycle:compiler:$architectureComponentsVersion"

        val rxJava = "io.reactivex.rxjava2:rxjava:$rxJavaVersion"

        val koin = "org.koin:koin-android:$koinVersion"
        val koinArchitecture = "org.koin:koin-android-architecture:$koinVersion"

        val rxBinding = "com.jakewharton.rxbinding2:rxbinding:$rxBindingVersion"
        val rxBindingKotlin = "com.jakewharton.rxbinding2:rxbinding-kotlin:$rxBindingVersion"

        val rxRelay = "com.jakewharton.rxrelay2:rxrelay:$rxRelayVersion"
    }

    object TestLibs {
        val jUnit = "junit:junit:$jUnitVersion"
        val espresso = "com.android.support.test.espresso:espresso-core:$espressoVersion"
    }

}