package com.example.myapplication.features.ui

import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.FragmentActivity

// Created by Gbenga Oladipupo(Devmike01) on 5/16/21.

@Composable
fun StateCodelabTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit,
        activity: FragmentActivity ? = null
) {

    activity?.apply {
        window.statusBarColor = MaterialTheme.colors.primaryVariant.toArgb()
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
    background = white100
)