package com.example.myapplication.features.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

abstract class ThemeBaseActivity: AppCompatActivity() {

    @Preview @Composable abstract fun MainContent() : Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            StateCodelabTheme {
                MainContent()
            }
        }
    }
}