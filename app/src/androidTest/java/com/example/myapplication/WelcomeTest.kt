package com.example.myapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.example.myapplication.features.main.MainActivity
import org.junit.Rule
import org.junit.Test

class WelcomeTest {


    @get:Rule
    val composableRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testMainContent(){
        Thread.sleep(1000)
        val button = composableRule.onNode(hasTestTag("shdsjdjsj"), useUnmergedTree = true)//.assertIsDisplayed()
        button.assertIsDisplayed()
        button.assertExists()
    }

}