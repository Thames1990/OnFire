import Config.Android.applicationId
import Config.Android.buildToolsVersion
import Config.Android.compileSdkVersion
import Config.Android.minSdkVersion
import Config.Android.targetSdkVersion
import Config.Android.versionCode
import Config.Android.versionName
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.setValue

plugins {
    id("com.android.application")
    id("com.gladed.androidgitversion")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(Config.Android.compileSdkVersion)
    buildToolsVersion(Config.Android.buildToolsVersion)

    defaultConfig {
        applicationId = Config.Android.applicationId
        minSdkVersion(Config.Android.minSdkVersion)
        targetSdkVersion(Config.Android.targetSdkVersion)

        androidGitVersion.prefix = "v"
        versionCode = androidGitVersion.code()
        versionName = androidGitVersion.name()

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
    implementation(Config.Libs.RxJava.java)
    implementation(Config.Libs.RxJava.binding)
    implementation(Config.Libs.RxJava.bindingKotlin)
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