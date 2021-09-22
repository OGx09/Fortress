package com.example.myapplication.features.main

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.Lifecycle
import com.example.myapplication.features.ui.StateCodelabTheme
import com.example.myapplication.features.ui.screens.*
import com.example.myapplication.utils.FingerprintUtils
import com.example.myapplication.utils.Routes
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.*

suspend fun ScaffoldState.showSnackbar(message: String){
    this.snackbarHostState.showSnackbar(message = message)
}

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity() {

    val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var fingerprintUtil : FingerprintUtils

    companion object{
        const val PAGE_TRANSITION_TIME : Int = 500
    }


    @ExperimentalAnimationApi
    @ExperimentalCoroutinesApi
    @ExperimentalComposeApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.run {
            readSavedPasswordDetails()
            checkForExistingLogin()
        }

        if(Build.VERSION.SDK_INT >= 30){
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }else{
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        supportActionBar?.hide()

        setContent{

            val scaffoldState = rememberScaffoldState()

            StateCodelabTheme(content = { MainContent() }, activity = this)
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalCoroutinesApi
    @ExperimentalComposeApi
    @Preview
    @Composable
    private fun MainContent() {

        val scaffoldState = rememberScaffoldState()
        val navControllerState = rememberAnimatedNavController()
        val systemUiController = rememberSystemUiController()
        val colorState = remember{mutableStateOf<Color?>(null)}

        SideEffect {
            // Update all of the system bar colors to be transparent, and use
            // dark icons if we're in light theme
            systemUiController.setSystemBarsColor(
                color = colorState.value ?: Color.Transparent,
            )
            // setStatusBarsColor() and setNavigationBarsColor() also exist
        }



        Scaffold(
            scaffoldState = scaffoldState,
        ) {

            val currentPageOpacity = remember{ mutableStateOf(0f)}
            AnimatedNavHost(navController = navControllerState,
                startDestination = Routes.SPLASH_SCREEN,   enterTransition = { initial, target ->
                    if(target.destination.route == Routes.PASSWORD_MAIN && initial.destination.route == Routes.SPLASH_SCREEN){
                        fadeIn(animationSpec = tween(PAGE_TRANSITION_TIME))
                    }else{
                        slideInVertically(initialOffsetY = { 1000 }, animationSpec = tween(PAGE_TRANSITION_TIME))
                    }
                }, exitTransition = { initial, target ->
                    if(target.destination.route == Routes.PASSWORD_MAIN && initial.destination.route == Routes.SPLASH_SCREEN){
                        fadeOut(animationSpec = tween(PAGE_TRANSITION_TIME))
                    }else{
                        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(PAGE_TRANSITION_TIME))
                    }

                },
                popEnterTransition = { initial, target ->
                    if(target.destination.route == Routes.PASSWORD_MAIN && initial.destination.route == Routes.SPLASH_SCREEN){
                        fadeIn(animationSpec = tween(PAGE_TRANSITION_TIME))
                    }else{
                        slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(PAGE_TRANSITION_TIME))
                    }
                },
                popExitTransition = { initial, target ->
                    if(target.destination.route == Routes.PASSWORD_MAIN && initial.destination.route == Routes.SPLASH_SCREEN){
                        fadeOut(animationSpec = tween(700))
                    }else{
                        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(PAGE_TRANSITION_TIME))
                    }
                }) {
                composable(Routes.SPLASH_SCREEN ){MainSplashScreen(navControllerState, this@MainActivity)}
                composable(Routes.WELCOME) {WelcomePage(this@MainActivity, viewModel, navControllerState)}
                composable(Routes.PASSWORD_MAIN,) {MainPasswordList(this@MainActivity, colorState, navControllerState, scaffoldState, currentPageOpacity) }
                composable(Routes.PASSWORD_DETAILS) { PasswordDetails(this@MainActivity, navControllerState, viewModel) }
                composable(Routes.ADD_NEW_PASSWORD) {
                    AddNewPassword(this@MainActivity, viewModel, navControllerState, scaffoldState, 1f)
                }
            }
        }

    }

}


