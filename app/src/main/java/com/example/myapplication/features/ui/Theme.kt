package com.example.myapplication.features.ui

import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.features.main.MainActivity

// Created by Gbenga Oladipupo(Devmike01) on 5/16/21.

@Composable
fun StateCodelabTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit,
        activity: FragmentActivity ? = null
) {

    activity?.apply {
        window.statusBarColor = Color.Transparent.toArgb()
        val colors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

        MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content,
        )
    }

}

@Composable
fun DefaultTopbar(backgroundColor: Color? =null,  title: String? = null,
                  onClick: (() -> Unit)? = null,
                  navControllerState: NavController) = TopAppBar(
    title = {
        Text(text = title ?: "Back")
    },
    navigationIcon = {
        IconButton(onClick = {
            if (onClick == null) {
                navControllerState.popBackStack()
            }else{
                onClick.invoke()
            }

        }) {
            Icon(imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back Btn")
        }
    },
    backgroundColor = backgroundColor ?: MaterialTheme.colors.background,
    contentColor = MaterialTheme.colors.primary,
    elevation = 0.dp
)

private val DarkColorPalette = darkColors(
    primary = white100,
    primaryVariant = purple700,
    secondary = white100,
    surface = grey900,
    background = grey900,
)

private val LightColorPalette = lightColors(
        primary = grey800,
        primaryVariant = purple700,
        secondary = grey800,
    surface = white100,
    background = white100,
)