package com.example.myapplication.features.main

import androidx.lifecycle.ViewModel
import com.example.myapplication.features.repository.FortressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Created by Gbenga Oladipupo(Devmike01) on 5/16/21.

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: FortressRepository): ViewModel(){

}