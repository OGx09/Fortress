package com.example.myapplication.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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