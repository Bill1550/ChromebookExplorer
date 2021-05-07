buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin.main}")
        classpath("com.android.tools.build:gradle:${Versions.Android.gradle_plugin}")
    }
}

group = "com.loneoaktech.apps"
version = "1.0"

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}