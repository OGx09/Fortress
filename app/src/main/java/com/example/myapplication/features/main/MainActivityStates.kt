package com.example.myapplication.features.main

import androidx.lifecycle.LiveData

interface MainActivityViewStates {

    fun welcome(username: String)

    fun saveWelcomeUsername(username: String)
}