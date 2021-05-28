package com.example.myapplication.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.myapplication.features.ui.StateCodelabTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
    fun Content(){
        StateCodelabTheme{
            Surface {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LazyColumn(contentPadding = PaddingValues(all = Dp(value = 20f))) {
                        items(10){index ->
                            Text("helllo $index")
                        }
                    }
                }
            }
        }
    }
}