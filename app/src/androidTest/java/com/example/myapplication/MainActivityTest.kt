package com.example.myapplication

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.ui.StateCodelabTheme
import com.example.myapplication.features.ui.screens.WelcomePage
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composableRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalAnimationApi
    @Test
    fun testMainContent(){
        val mainActivity = composableRule.activity
        composableRule.setContent {
            StateCodelabTheme(darkTheme = false,
                activity = mainActivity, content = {
                    WelcomePage(activity = mainActivity,
                    viewModel = mainActivity.viewModel,
                    rememberAnimatedNavController())

                    val button = composableRule.onNode(hasTestTag("shdsjdjsj"), useUnmergedTree = true)//.assertIsDisplayed()
                    button.assertIsDisplayed()
                    button.performClick()
                       // navControllerState = mainActivity.navControllerState)
                })

        }
    }

    @Test
    fun test_sample(){
        assertNull(composableRule)
        assertNotNull(composableRule)
    }

}