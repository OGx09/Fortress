
object Versions {
    const val lifecycle = "2.3.1"
    const val junit ="4.13.2"
    const val kotlin_stdlib = "1.5.10"
    const val room_version = "2.3.0"
    const val hilt_version = "2.35"
    const val biometric = "1.1.0"
    const val gson ="2.8.7"
    const val composable ="1.0.0-beta09"
    const val navigation ="2.3.5"
    const val retrofit ="2.9.0"
    const val coil ="1.3.0"
}

object Libs{
    val deps = arrayOf(
        "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_stdlib}",
        "androidx.core:core-ktx:1.3.2",
        "androidx.appcompat:appcompat:1.2.0",
        "com.google.android.material:material:1.3.0",
        "androidx.compose.foundation:foundation:${Versions.composable}",
        "androidx.activity:activity-compose:1.3.0-alpha07",
        "androidx.compose.ui:ui-tooling:${Versions.composable}",
        "androidx.compose.runtime:runtime:${Versions.composable}",
        "androidx.compose.compiler:compiler:${Versions.composable}",
        "androidx.compose.material:material:${Versions.composable}",
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}",
        "androidx.room:room-ktx:${Versions.room_version}",
        "androidx.biometric:biometric:${Versions.biometric}",
        "com.google.dagger:hilt-android:${Versions.hilt_version}",
        "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin_stdlib}",
        "com.google.code.gson:gson:${Versions.gson}",
        "androidx.compose.runtime:runtime-livedata:${Versions.composable}",
        "androidx.navigation:navigation-ui-ktx:${Versions.navigation}",
        "androidx.navigation:navigation-compose:2.4.0-alpha04",
        "com.squareup.retrofit2:retrofit:${Versions.retrofit}",
        "com.squareup.retrofit2:converter-gson:${Versions.retrofit}",
        "io.coil-kt:coil-compose:${Versions.coil}"
        //"androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    )

    val testDeps = arrayOf( "junit:junit:${Versions.junit}", "androidx.room:room-testing:${Versions.room_version}")

    val androidTestDeps = arrayOf("androidx.test.ext:junit:1.1.2",
        "androidx.compose.ui:ui-test-junit4:${Versions.composable}",
        "androidx.compose.ui:ui-test-manifest:${Versions.composable}")

    val kaptDeps = arrayOf("androidx.room:room-compiler:${Versions.room_version}",
        "com.google.dagger:hilt-android-compiler:${Versions.hilt_version}")

}