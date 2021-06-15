package com.example.myapplication

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.myapplication.features.main.MainActivity
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @get:Rule
    val composableRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun check_AddPassword() {
        assertEquals(4, 2 + 2)
    }
}