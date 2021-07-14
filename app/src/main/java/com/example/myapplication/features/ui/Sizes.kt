package com.example.myapplication.features.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp


object Sizes {
    val normal = 10.dp
    val medium = 20.dp
    val large = 30.dp
}

object Paddings{
    val normalStart = PaddingValues(start = Sizes.normal)
    val normalEnd= PaddingValues(end = Sizes.normal)
    val normalTop = PaddingValues(top = Sizes.normal)
    val normalBottom = PaddingValues(bottom = Sizes.normal)
    val normalAll = PaddingValues(Sizes.normal)


    val medium = PaddingValues(start = Sizes.medium)
    val mediumEnd= PaddingValues(end = Sizes.medium)
    val mediumTop = PaddingValues(top = Sizes.medium)
    val mediumBottom = PaddingValues(bottom = Sizes.medium)
    val mediumAll = PaddingValues(Sizes.medium)
}