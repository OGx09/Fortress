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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import javax.inject.Inject

// Created by Gbenga Oladipupo(Devmike01) on 5/16/21.

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: FortressRepository): ViewModel(){

    private val _savePasswordEntityLiveData = MutableLiveData<List<PasswordEntity>>()
    val savePasswordEntityLiveData : LiveData<List<PasswordEntity>> = _savePasswordEntityLiveData

    private val _savePasswordDataLiveData = MutableLiveData<List<FortressModel>>()
    val savePasswordDataLiveData : LiveData<List<FortressModel>> = _savePasswordDataLiveData


    fun readSavedPasswords(cipher: Cipher, id: Int){
        viewModelScope.launch(Dispatchers.Main) {
            val allPasswords = repository.fetchPasswordDetails(cipher = cipher, id =id)
            Log.d("FortressRepository", "FortressRepositoryImpl ${allPasswords}")
            allPasswords?.apply {
                _savePasswordDataLiveData.postValue( this)
            }
        }
    }


    fun readSavedPasswordDetails(){
        viewModelScope.launch{
            val allPasswords = repository.fetchAllEncryptedPasswords()
            //Log.d("FortressRepository", "FortressRepositoryImpl ${allPasswords}")
            allPasswords.apply {
                _savePasswordEntityLiveData.postValue( this)
            }
        }
    }
}