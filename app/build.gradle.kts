
plugins {
    id ("com.android.application")
    kotlin ("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    defaultConfig {
//        configurations.all{
//            resolutionStrategy {
//                this.force("androidx.core:core-ktx:1.6.0")
//            }
//        }
        applicationId ="com.example.myapplication"
        minSdk = (23)
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner( "androidx.test.runner.AndroidJUnitRunner")

        // The following argument makes the Android Test Orchestrator run its
        // "pm clear" command after each test invocation. This command ensures
        // that the app's state is completely cleared between tests.
        testInstrumentationRunnerArguments(mutableMapOf(
            "clearPackageData" to "true"
        ))

        testOptions {
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
        }


    }

    buildFeatures {
        compose = true
    }

    kapt {
        javacOptions {
            // These options are normally set automatically via the Hilt Gradle plugin, but we
            // set them manually to workaround a bug in the Kotlin 1.5.20
            option("-Adagger.fastInit=ENABLED")
            option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
        }
    }

    composeOptions {
        kotlinCompilerVersion = Versions.kotlinStdlib
        kotlinCompilerExtensionVersion = Versions.composable

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
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    ndkVersion = "21.3.6528147"

    configurations.all {
        resolutionStrategy.eachDependency {
            val requested = this.requested
            if(requested.group == "org.jetbrains.kotlin"  && requested.name == "kotlin-reflect"){
                this.useVersion(Versions.kotlinStdlib)
            }
        }
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
        exclude ("META-INF/AL2.0")
        exclude ("META-INF/LGPL2.1")
        exclude ("META-INF/DEPENDENCIES")
        exclude ("META-INF/LICENSE")
        exclude ("META-INF/LICENSE.txt")
        exclude ("META-INF/license.txt")
        exclude ("META-INF/NOTICE")
        exclude ("META-INF/NOTICE.txt")
        exclude ("META-INF/notice.txt")
        exclude ("META-INF/ASL2.0")
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

    Libs.debugImpl.forEach{
        debugImplementation(it)
    }


}
