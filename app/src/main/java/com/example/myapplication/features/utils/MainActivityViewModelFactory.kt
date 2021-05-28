package com.example.myapplication.features.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.repository.FortressRepository
import java.lang.IllegalArgumentException

class MainActivityViewModelFactory(private val repository: FortressRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            MainActivityViewModel(repository = repository) as T
        }else{
            throw IllegalArgumentException("viewmodel not found")
        }
    }
}