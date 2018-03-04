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
    compileSdkVersion(Android.compileSdkVersion)
    buildToolsVersion(Android.buildToolsVersion)

    defaultConfig {
        applicationId = Android.applicationId
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
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
    implementation(Libs.kau)
    implementation(Libs.kotlin_std)
    // Support Library
    implementation(Libs.SupportLibrary.appcompat)
    implementation(Libs.SupportLibrary.cardview)
    implementation(Libs.SupportLibrary.design)
    implementation(Libs.SupportLibrary.constraintLayout)
    implementation(Libs.SupportLibrary.recyclerview)
    // ViewModel and LiveData
    implementation(Libs.ArchitectureComponents.Lifecycle.extensions)
    kapt(Libs.ArchitectureComponents.Lifecycle.compiler)
    // Room
    implementation(Libs.ArchitectureComponents.Room.runtime)
    implementation(Libs.ArchitectureComponents.Room.rxJava)
    kapt(Libs.ArchitectureComponents.Room.compiler)
    // RxJava
    implementation(Libs.RxJava.core)
    implementation(Libs.RxJava.Binding.core)
    implementation(Libs.RxJava.Binding.kotlin)
    implementation(Libs.RxJava.relay)
    // Koin
    implementation(Libs.Koin.android)
    implementation(Libs.Koin.androidArchitecture)
    // Test
    testImplementation(TestLibs.jUnit)
    // Instrumentation test
    androidTestImplementation(TestLibs.espresso)
    androidTestImplementation(TestLibs.jUnit)
}