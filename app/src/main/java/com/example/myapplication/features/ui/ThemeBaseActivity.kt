package com.example.myapplication.features.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview


data class FloatingBtn(var resId: Int =0, var useFab: Boolean = false)

abstract class ThemeBaseActivity: AppCompatActivity() {

    @Preview @Composable abstract fun MainContent() : Unit

    open fun fabResId(): Int? = null

    abstract fun onFabClick()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            StateCodelabTheme(content = {Scaffold(
                modifier = Modifier.background(Color.Blue),
                floatingActionButtonPosition = FabPosition.End,
                floatingActionButton = {
                    fabResId()?.apply {
                        FloatingActionButton(onClick = { onFabClick() }) {
                            Icon(painter = painterResource(id = this), contentDescription = "")
                        }
                    }
                },
                content = {
                    MainContent()
                }
            )}, activity = this)
        }
    }
}
