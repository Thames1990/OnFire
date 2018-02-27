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
        versionCode = Config.Android.versionCode
        versionName = Config.Android.versionName

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

    implementation(Config.Libs.appcompat)
    implementation(Config.Libs.cardview)
    implementation(Config.Libs.design)
    implementation(Config.Libs.constraintLayout)
    implementation(Config.Libs.recyclerview)

    implementation(Config.Libs.lifecycleExtensions)
    implementation(Config.Libs.roomRuntime)
    implementation(Config.Libs.roomRxJava)
    kapt(Config.Libs.roomCompiler)
    kapt(Config.Libs.lifecycleCompiler)

    implementation(Config.Libs.rxJava)

    implementation(Config.Libs.koin)
    implementation(Config.Libs.koinArchitecture)

    implementation(Config.Libs.rxBinding)
    implementation(Config.Libs.rxBindingKotlin)

    implementation(Config.Libs.rxRelay)

    testImplementation(Config.TestLibs.jUnit)
    testImplementation(Config.TestLibs.espresso)

    androidTestImplementation(Config.TestLibs.espresso)
}