
object Versions {
    const val lifecycle = "2.3.1"
    const val junit ="4.13.2"
    const val kotlin_stdlib = "1.4.32"
    const val compose ="1.0.0-beta07"
    const val room_version = "2.3.0"
    const val hilt_version = "2.35"
    const val biometric = "1.1.0"
    const val gson ="2.8.7"

}

object Libs{
    val deps = arrayOf(
        "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_stdlib}",
        "androidx.core:core-ktx:1.3.2",
        "androidx.appcompat:appcompat:1.2.0",
        "com.google.android.material:material:1.3.0",
        "androidx.compose.foundation:foundation:${Versions.compose}",
        "androidx.activity:activity-compose:1.3.0-alpha07",
        "androidx.compose.ui:ui-tooling:${Versions.compose}",
        "androidx.compose.runtime:runtime:${Versions.compose}",
        "androidx.compose.compiler:compiler:${Versions.compose}",
        "androidx.compose.material:material:${Versions.compose}",
        "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle}",
        "androidx.room:room-ktx:${Versions.room_version}",
        "androidx.biometric:biometric:${Versions.biometric}",
        "com.google.dagger:hilt-android:${Versions.hilt_version}",
        "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin_stdlib}",
        "com.google.code.gson:gson:${Versions.gson}"
        //"androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    )

    val testDeps = arrayOf( "junit:junit:${Versions.junit}", "androidx.room:room-testing:${Versions.room_version}")

    val androidTestDeps = arrayOf("androidx.test.espresso:espresso-core:3.3.0",
        "androidx.test.ext:junit:1.1.2")

    val kaptDeps = arrayOf("androidx.room:room-compiler:${Versions.room_version}",
        "com.google.dagger:hilt-android-compiler:${Versions.hilt_version}")

}