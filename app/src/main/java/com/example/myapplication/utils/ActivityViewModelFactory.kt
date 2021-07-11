package com.example.myapplication.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.managepassword.AddPaswordActivityViewModel
import com.example.myapplication.repository.FortressRepository
import java.lang.IllegalArgumentException

class ActivityViewModelFactory(private val repository: FortressRepository,
                               private val fingerprintUtils: FingerprintUtils? =null): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            MainActivityViewModel(repository = repository) as T
        }else if(modelClass.isAssignableFrom(AddPaswordActivityViewModel::class.java)){
            Log.d("ActivityViewModelFactory", "_$fingerprintUtils")
            if (fingerprintUtils != null){
                AddPaswordActivityViewModel(repository = repository) as T
            }
            throw IllegalArgumentException("fingerprintUtils not found")
        }else{
            throw IllegalArgumentException("viewmodel not found")
        }
    }
}