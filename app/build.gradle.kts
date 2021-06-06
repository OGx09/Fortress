
plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id("kotlin-kapt")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")

    // kotlin("jvm") version "1.5.0"
}

android {
    compileSdk =( 30)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId ="com.example.myapplication"
        minSdk = (23)
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerVersion = Versions.kotlin_stdlib
        kotlinCompilerExtensionVersion = Versions.compose

    }


    buildTypes {
        getByName("release"){
            isMinifyEnabled = false
            proguardFiles( getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {
  Libs.deps.forEach { library ->
      implementation(library)
  }

    Libs.testDeps.forEach { testLib ->
        testImplementation(testLib)
    }

    Libs.androidTestDeps.forEach { androidTestLib ->
        androidTestImplementation(androidTestLib)
    }

    Libs.kaptDeps.forEach {
        kapt(it)
    }

}