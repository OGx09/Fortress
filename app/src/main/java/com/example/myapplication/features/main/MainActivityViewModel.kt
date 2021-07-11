package com.example.myapplication.features.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.repository.models.FortressModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

// Created by Gbenga Oladipupo(Devmike01) on 5/16/21.

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: FortressRepository): ViewModel(){

    private val _savePasswordLiveData = MutableLiveData<List<PasswordEntity>>()
    val savePassword : LiveData<List<PasswordEntity>> = _savePasswordLiveData

    init {
        readSavedPasswords()
    }

    fun readSavedPasswords(){
        viewModelScope.launch(Dispatchers.Main) {
            val allPasswords =repository.fetchAllPasswords()
            Log.d("FortressRepository", "FortressRepositoryImpl ${allPasswords.size}")
            _savePasswordLiveData.postValue( allPasswords)
        }
    }
}