package com.example.myapplication.features.main

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.features.ui.StateCodelabTheme
import com.example.myapplication.features.ui.screens.*
import com.example.myapplication.utils.FingerprintUtils
import com.example.myapplication.utils.Routes
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity() {

    val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var fingerprintUtil : FingerprintUtils

    lateinit var navController: NavHostController



    @ExperimentalCoroutinesApi
    @ExperimentalComposeApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(Build.VERSION.SDK_INT >= 30){
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            //SOFT_INPUT_ADJUST_PAN
        }else{
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        supportActionBar?.hide()
        setContent{
            StateCodelabTheme(content = { MainContent() }, activity = this)
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalComposeApi
    @Preview
    @Composable
    private fun MainContent() {
        navController = rememberNavController()

        var screenChangeAnimState by  remember { mutableStateOf(0F) }

        val snackbarHostState = remember { SnackbarHostState()}

        LaunchedEffect(key1 = viewModel.messageState ){
            viewModel.messageState.collect {
                snackbarHostState.showSnackbar(it)
            }
        }

        NavHost(navController = navController,
            startDestination = Routes.SPLASH_SCREEN) {
            composable(Routes.SPLASH_SCREEN){MainSplashScreen(navController, this@MainActivity)}
            composable(Routes.WELCOME) {WelcomePage(this@MainActivity, viewModel, navController)}
            composable(Routes.PASSWORD_MAIN) {MainPasswordList(this@MainActivity, viewModel, navController) }
            composable(Routes.PASSWORD_DETAILS) { PasswordDetails(this@MainActivity, viewModel) }
            composable(Routes.ADD_NEW_PASSWORD) {
                screenChangeAnimState = 1f
                AddNewPassword(this@MainActivity, viewModel)
            }
        }

    }
}


