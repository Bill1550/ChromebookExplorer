buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")
        classpath("com.android.tools.build:gradle:4.2.0")
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