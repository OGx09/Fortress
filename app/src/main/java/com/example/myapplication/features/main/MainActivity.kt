package com.example.myapplication.features.main

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import com.example.myapplication.features.ui.ThemeBaseActivity
import com.example.myapplication.repository.database.PasswordEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.features.detials.MainPasswordList
import com.example.myapplication.features.detials.PasswordDetails
import com.example.myapplication.utils.Routes
//import com.example.myapplication.features.detials.PasswordDetailsFragment
import java.util.*

@AndroidEntryPoint
class MainActivity @Inject constructor() : ThemeBaseActivity() {

    val  viewModel: MainActivityViewModel by viewModels()


    @ExperimentalCoroutinesApi
    @ExperimentalComposeApi
    @Composable
    override fun MainContent() {
        val navController: NavHostController = rememberNavController()

        NavHost(navController = navController,
            startDestination = Routes.PASSWORD_MAIN) {
            composable(Routes.PASSWORD_MAIN) {MainPasswordList(this@MainActivity, viewModel, navController) }
            composable(Routes.PASSWORD_DETAILS) { PasswordDetails(this@MainActivity, viewModel) }
        }
    }


    override fun fabResId(): Int{
        return android.R.drawable.ic_input_add
    }


}