
object Versions {
    const val lifecycle = "2.3.1"
    const val junit ="4.13.2"
    const val androidJunit ="1.1.2"
    const val kotlinStdlib = "1.5.10"
    const val roomVersion = "2.3.0"
    const val hiltVersion = "2.35"
    const val biometric = "1.1.0"
    const val gson ="2.8.7"
    const val composable ="1.0.0-beta09"
    const val navigation ="2.3.5"
    const val retrofit ="2.9.0"
    const val coil ="1.3.0"
    const val datastore ="1.0.0-rc01"
    const val androidXCore ="1.3.2"
    const val androidXCompat ="1.2.0"
    const val androidMaterial ="1.3.0"
    const val composeActivity= "1.3.0-alpha07"
    const val coroutine ="1.3.9"
    const val mockito ="1.10.19"
    const val testCore ="1.0.0"
    const val androidXCoreTest ="2.1.0"
    const val powerMockito ="2.0.7"
    const val accompanist ="0.18.0"
}

object Libs{
    val deps = arrayOf(
        "androidx.datastore:datastore-preferences:${Versions.datastore}",
        "androidx.datastore:datastore-preferences-core:${Versions.datastore}",
        "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinStdlib}",
        "androidx.core:core-ktx:${Versions.androidXCore}",
        "androidx.appcompat:appcompat:${Versions.androidXCompat}",
        "com.google.android.material:material:${Versions.androidMaterial}",
        "androidx.compose.foundation:foundation:${Versions.composable}",
        "androidx.activity:activity-compose:${Versions.composeActivity}",
        "androidx.compose.ui:ui-tooling:${Versions.composable}",
        "androidx.compose.runtime:runtime:${Versions.composable}",
        "androidx.compose.compiler:compiler:${Versions.composable}",
        "androidx.compose.material:material:${Versions.composable}",
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}",
        "androidx.room:room-ktx:${Versions.roomVersion}",
        "androidx.biometric:biometric:${Versions.biometric}",
        "com.google.dagger:hilt-android:${Versions.hiltVersion}",
        "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlinStdlib}",
        "com.google.code.gson:gson:${Versions.gson}",
        "androidx.compose.runtime:runtime-livedata:${Versions.composable}",
        "androidx.navigation:navigation-ui-ktx:${Versions.navigation}",
        "androidx.navigation:navigation-compose:2.4.0-alpha04",
        "com.squareup.retrofit2:retrofit:${Versions.retrofit}",
        "com.squareup.retrofit2:converter-gson:${Versions.retrofit}",
        "io.coil-kt:coil-compose:${Versions.coil}",
        "androidx.compose.animation:animation:${Versions.composable}",
        "androidx.compose.animation:animation-core:${Versions.composable}",
        "com.google.accompanist:accompanist-navigation-animation:${Versions.accompanist}"
        //"androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    )

    val testDeps = arrayOf( "junit:junit:${Versions.junit}",
        "androidx.room:room-testing:${Versions.roomVersion}",
        "org.mockito:mockito-core:${Versions.mockito}",
        "androidx.test:core:${Versions.testCore}",
        "androidx.arch.core:core-testing:${Versions.androidXCoreTest}",
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutine}",
        "org.mockito:mockito-all:1.10.19",
        "org.powermock:powermock-module-junit4:${Versions.powerMockito}",
        "org.powermock:powermock-module-junit4-rule:${Versions.powerMockito}"
        , "org.powermock:powermock-api-mockito2:${Versions.powerMockito}",
        "org.powermock:powermock-classloading-xstream:1.6.6"
    )

    val androidTestDeps = arrayOf("androidx.test.ext:junit:${Versions.androidJunit}",
        "androidx.compose.ui:ui-test-junit4:${Versions.composable}",
        "androidx.compose.ui:ui-test-manifest:${Versions.composable}",
        "androidx.compose.ui:ui-test-junit4:${Versions.composable}"
    )

    val kaptDeps = arrayOf("androidx.room:room-compiler:${Versions.roomVersion}",
        "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}")

    val debugImpl = arrayOf("androidx.compose.ui:ui-test-manifest:${Versions.composable}")

}