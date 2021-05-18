
plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id("kotlin-kapt")
}

val compose_version = "1.0.0-beta06"
val projectArch = "2.3.1"


android {
    compileSdkVersion( 30)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId ="com.example.myapplication"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode(1)
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
       // kotlinCompilerExtensionVersion = "1.0.0-beta05"
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.0")
    implementation ("androidx.core:core-ktx:1.3.2")
    implementation( "androidx.appcompat:appcompat:1.2.0")
    implementation ("com.google.android.material:material:1.3.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.4")
    testImplementation ("junit:junit:4.+")
    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
    implementation ("androidx.lifecycle:lifecycle-runtime:$projectArch")
    // implementation ("androidx.lifecycle:lifecycle-extensions:$projectArch")
    implementation ("androidx.activity:activity-compose:1.3.0-alpha07")
    //kapt("androidx.lifecycle:lifecycle-compiler:$projectArch")
    implementation ("androidx.ui:ui-tooling:$compose_version")
    implementation ("androidx.compose.runtime:runtime:$compose_version")
    implementation ("androidx.compose.compiler:compiler:$compose_version")

    //implementation("androidx.compose.compiler:compiler:1.0.0-beta06")

}