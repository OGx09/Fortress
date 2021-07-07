package com.example.myapplication.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.myapplication.features.managepassword.AddPasswordActivity
import com.example.myapplication.features.ui.StateCodelabTheme
import com.example.myapplication.features.ui.ThemeBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : ThemeBaseActivity() {

    private val viewModel: MainActivityViewModel by viewModels()


    @ExperimentalCoroutinesApi
    @ExperimentalComposeApi
    @Composable
    override fun MainContent() {
        ContentBody()
    }


    override fun fabResId(): Int{
        return android.R.drawable.ic_input_add
    }


    @Preview
    @Composable
    fun ContentBody() {
        Column(verticalArrangement = Arrangement.Center) {
            LazyColumn(contentPadding = PaddingValues(all = Dp(value = 20f))) {
                items(10) { index ->
                    Text("helllo $index")
                }
            }
        }
    }



}