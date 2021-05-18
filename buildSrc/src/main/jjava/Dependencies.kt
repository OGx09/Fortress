
object Versions {
    val lifecycle = "2.3.1"
    val junit ="4.13.2"
    val kotlin_stdlib = "1.4.32"
}

object Libs{
    val deps = arrayOf(
        "org.jetbrains.kotlin:kotlin-stdlib:1.4.32",
        "androidx.core:core-ktx:1.3.2", "androidx.appcompat:appcompat:1.2.0",
        "com.google.android.material:material:1.3.0",
        "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle}",
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    )

    val testDeps = arrayOf( "junit:junit:${Versions.junit}")

    val androidTestDeps = arrayOf("androidx.test.espresso:espresso-core:3.3.0",
        "androidx.test.ext:junit:1.1.2")

    val kaptDeps = arrayOf("androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}")

}