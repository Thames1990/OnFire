import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.setValue
import org.gradle.kotlin.dsl.repositories

buildscript {
    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Config.BuildPlugins.androidGitVersion)
        classpath(Config.BuildPlugins.androidGradle)
        classpath(Config.BuildPlugins.kotlinGradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}