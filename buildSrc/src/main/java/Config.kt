@Suppress("unused")
object Config {

    private const val kotlinVersion = "1.2.21"

    object BuildPlugins {
        const val androidGitVersion =
            "gradle.plugin.com.gladed.gradle.androidgitversion:gradle-android-git-version:0.4.3"
        const val androidGradle = "com.android.tools.build:gradle:3.0.1"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
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

    object Libs {
        const val kotlin_std = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"

        object SupportLibrary {
            private const val support = "27.1.0"
            const val appcompat = "com.android.support:appcompat-v7:$support"
            const val cardview = "com.android.support:cardview-v7:$support"
            const val constraintLayout = "com.android.support.constraint:constraint-layout:1.0.2"
            const val design = "com.android.support:design:$support"
            const val recyclerview = "com.android.support:recyclerview-v7:$support"
        }

        object ArchitectureComponents {

            object Lifecycle {
                private const val lifecycle = "1.1.0"
                const val extensions = "android.arch.lifecycle:extensions:$lifecycle"
                const val compiler = "android.arch.lifecycle:compiler:$lifecycle"
            }

            object Room {
                private const val room = "1.0.0"
                const val compiler = "android.arch.persistence.room:compiler:$room"
                const val runtime = "android.arch.persistence.room:runtime:$room"
                const val rxJava = "android.arch.persistence.room:rxjava2:$room"
            }

        }

        object RxJava {
            private const val rxBinding = "2.1.1"
            const val java = "io.reactivex.rxjava2:rxjava:2.1.10"
            const val binding = "com.jakewharton.rxbinding2:rxbinding:$rxBinding"
            const val bindingKotlin = "com.jakewharton.rxbinding2:rxbinding-kotlin:$rxBinding"
            const val relay = "com.jakewharton.rxrelay2:rxrelay:2.0.0"
        }

        object Koin {
            private const val koin = "0.8.2"
            const val android = "org.koin:koin-android:$koin"
            const val androidArchitecture = "org.koin:koin-android-architecture:$koin"
        }
    }

    object TestLibs {
        const val espresso = "com.android.support.test.espresso:espresso-core:3.0.1"
        const val jUnit = "junit:junit:4.12"
    }

}