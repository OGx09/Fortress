package com.example.myapplication.features.main

import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import com.example.myapplication.features.ui.ThemeBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.features.screens.AddNewPassword
import com.example.myapplication.features.screens.MainPasswordList
import com.example.myapplication.features.screens.PasswordDetails
import com.example.myapplication.utils.FingerprintUtils
import com.example.myapplication.utils.Routes
import java.util.*

@AndroidEntryPoint
class MainActivity @Inject constructor() : ThemeBaseActivity() {

    val  viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var fingerprintUtil : FingerprintUtils

    lateinit var navController: NavHostController


    @ExperimentalCoroutinesApi
    @ExperimentalComposeApi
    @Composable
    override fun MainContent() {

        navController = rememberNavController()

        var screenChangeAnimState by  remember { mutableStateOf(0F) }

        NavHost(navController = navController,
            startDestination = Routes.PASSWORD_MAIN) {
            composable(Routes.PASSWORD_MAIN) {MainPasswordList(this@MainActivity, viewModel, navController) }
            composable(Routes.PASSWORD_DETAILS) { PasswordDetails(this@MainActivity, viewModel) }
            composable(Routes.ADD_NEW_PASSWORD) {
                screenChangeAnimState = 1f
                AddNewPassword(fingerprintUtil, this@MainActivity, viewModel, screenChangeAnimState)
            }
        }


    }


    override fun fabResId(): Int{
        return android.R.drawable.ic_input_add
    }

    override fun onFabClick() {
        navController.navigate(Routes.ADD_NEW_PASSWORD)
    }


}