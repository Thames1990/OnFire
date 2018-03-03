import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.setValue

apply { from("experimentalExtensions.gradle") }

plugins {
    id("com.android.application")
    id("com.gladed.androidgitversion")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Config.Android.compileSdkVersion)
    buildToolsVersion(Config.Android.buildToolsVersion)

    defaultConfig {
        applicationId = Config.Android.applicationId
        minSdkVersion(Config.Android.minSdkVersion)
        targetSdkVersion(Config.Android.targetSdkVersion)
        versionCode = androidGitVersion.code()
        versionName = androidGitVersion.name()

        androidExtensions.isExperimental = true
        androidGitVersion.prefix = "v"
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(Config.Libs.kotlin_std)
    // Support Library
    implementation(Config.Libs.SupportLibrary.appcompat)
    implementation(Config.Libs.SupportLibrary.cardview)
    implementation(Config.Libs.SupportLibrary.design)
    implementation(Config.Libs.SupportLibrary.constraintLayout)
    implementation(Config.Libs.SupportLibrary.recyclerview)
    // ViewModel and LiveData
    implementation(Config.Libs.ArchitectureComponents.Lifecycle.extensions)
    kapt(Config.Libs.ArchitectureComponents.Lifecycle.compiler)
    // Room
    implementation(Config.Libs.ArchitectureComponents.Room.runtime)
    implementation(Config.Libs.ArchitectureComponents.Room.rxJava)
    kapt(Config.Libs.ArchitectureComponents.Room.compiler)
    // RxJava
    implementation(Config.Libs.RxJava.core)
    implementation(Config.Libs.RxJava.Binding.core)
    implementation(Config.Libs.RxJava.Binding.kotlin)
    implementation(Config.Libs.RxJava.relay)
    // Koin
    implementation(Config.Libs.Koin.android)
    implementation(Config.Libs.Koin.androidArchitecture)
    // Test
    testImplementation(Config.TestLibs.jUnit)
    // Instrumentation test
    androidTestImplementation(Config.TestLibs.espresso)
    androidTestImplementation(Config.TestLibs.jUnit)
}