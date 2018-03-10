@file:Suppress("unused")

object Versions {
    const val androidGitVersion = "0.4.3"
    const val koin = "0.9.0"
    const val kotlin = "1.2.30"
    const val lifecycle = "1.1.0"
    const val room = "1.0.0"
    const val rxBinding = "2.1.1"
    const val supportLibrary = "27.1.0"
}

object Android {
    const val buildToolsVersion = "27.0.3"
    const val minSdkVersion = 15
    const val targetSdkVersion = 27
    const val compileSdkVersion = 27
    const val applicationId = "xyz.thomasmohr.onfire"
    const val versionCode = 1
    const val versionName = "1.0"
}

object BuildPlugins {
    const val androidGradle = "com.android.tools.build:gradle:3.0.1"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object Libs {
    object SupportLibrary {
        const val appcompat = "com.android.support:appcompat-v7:${Versions.supportLibrary}"
        const val cardview = "com.android.support:cardview-v7:${Versions.supportLibrary}"
        const val constraintLayout = "com.android.support.constraint:constraint-layout:1.0.2"
        const val design = "com.android.support:design:${Versions.supportLibrary}"
        const val recyclerview = "com.android.support:recyclerview-v7:${Versions.supportLibrary}"
    }

    object ArchitectureComponents {
        object Lifecycle {
            const val extensions = "android.arch.lifecycle:extensions:${Versions.lifecycle}"
            const val compiler = "android.arch.lifecycle:compiler:${Versions.lifecycle}"
        }

        object Room {
            const val compiler = "android.arch.persistence.room:compiler:${Versions.room}"
            const val runtime = "android.arch.persistence.room:runtime:${Versions.room}"
            const val rxJava = "android.arch.persistence.room:rxjava2:${Versions.room}"
        }
    }

    object RxJava {
        const val core = "io.reactivex.rxjava2:rxjava:2.1.10"
        const val relay = "com.jakewharton.rxrelay2:rxrelay:2.0.0"

        object Binding {
            const val core = "com.jakewharton.rxbinding2:rxbinding:${Versions.rxBinding}"
            const val kotlin = "com.jakewharton.rxbinding2:rxbinding-kotlin:${Versions.rxBinding}"
        }
    }

    object Koin {
        const val android = "org.koin:koin-android:${Versions.koin}"
        const val androidArchitecture = "org.koin:koin-android-architecture:${Versions.koin}"
    }
}

object TestLibs {
    const val espresso = "com.android.support.test.espresso:espresso-core:3.0.1"
    const val jUnit = "junit:junit:4.12"
}