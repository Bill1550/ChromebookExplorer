plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

group = "com.loneoaktech.apps"
version = "1.0"

dependencies {
    implementation(project(":shared"))
//    implementation( Dependencies.android_coroutines )
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation( "androidx.core:core-ktx:1.6.0")

    implementation("androidx.activity:activity-ktx:1.2.3")
    implementation( "androidx.fragment:fragment-ktx:1.3.5")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha02")


}

android {
    compileSdkVersion(Versions.Android.compile_sdk)
    defaultConfig {
        applicationId = "com.loneoaktech.apps.chromebookexplorer"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}