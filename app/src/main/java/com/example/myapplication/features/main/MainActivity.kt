package com.example.myapplication.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.myapplication.features.managepassword.AddPasswordActivity
import com.example.myapplication.features.ui.StateCodelabTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DATATATTA", "${this::viewModel.isLateinit}")
        setContent {
            Content()
        }
    }

    @Preview
    @Composable
    fun Content() {
        StateCodelabTheme {
            Scaffold(
                floatingActionButtonPosition = FabPosition.End,
                floatingActionButton = {
                    FloatingActionButton(onClick = { AddPasswordActivity.start(this) }) {
                        Text("+")
                    }
                },
                content = {
                    ContentBody()
                }
            )
        }
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