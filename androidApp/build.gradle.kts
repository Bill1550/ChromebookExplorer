plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

group = "com.loneoaktech.apps"
version = "1.0"

dependencies {
    implementation(project(":shared"))
    implementation( Dependencies.coroutines )
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation( "androidx.core:core-ktx:1.5.0-rc02")
    implementation("androidx.activity:activity-ktx:1.2.3")
    implementation( "androidx.fragment:fragment-ktx:1.3.3")


}

android {
    compileSdkVersion(Versions.Android.compile_sdk)
    defaultConfig {
        applicationId = "com.loneoaktech.apps.androidApp"
        minSdkVersion(Versions.Android.min_sdk)
        targetSdkVersion(Versions.Android.target_sdk)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        viewBinding = true
    }
}