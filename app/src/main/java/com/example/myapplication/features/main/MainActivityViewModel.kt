package com.example.myapplication.features.main

import android.util.Base64
import android.util.Base64.encodeToString
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.repository.models.FortressModel
import com.example.myapplication.repository.models.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.crypto.Cipher
import javax.inject.Inject

// Created by Gbenga Oladipupo(Devmike01) on 5/16/21.

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: FortressRepository): ViewModel(){

    private val _savePasswordEntityLiveData = MediatorLiveData<List<PasswordEntity>>()
    val savePasswordEntityLiveData : MediatorLiveData<List<PasswordEntity>> = _savePasswordEntityLiveData

    private val _savePasswordDataLiveData = MutableLiveData<LoadingState<Boolean>>()
    val savePasswordDataLiveData : LiveData<LoadingState<Boolean>> = _savePasswordDataLiveData

    init {
        readSavedPasswordDetails()
    }


    fun readSavedPassword(cipher: Cipher, id: Int){
        viewModelScope.launch(Dispatchers.Main) {
            val allPasswords = repository.fetchPasswordDetails(cipher = cipher, id =id)
            Log.d("FortressRepository", "FortressRepositoryImpl ${allPasswords}")
            allPasswords?.apply {
              //  _savePasswordDataLiveData.postValue( this)
            }
        }
    }


    private fun readSavedPasswordDetails(){
        viewModelScope.launch{
            _savePasswordEntityLiveData.addSource(repository.fetchAllEncryptedPasswords()) {
                _savePasswordEntityLiveData.value = it
            }
        }
    }



    fun savePassword(websiteUrl: String, websiteName: String,
                     password: String, buzzWord: String,
                     username: String, cipher: Cipher){

        viewModelScope.launch(Dispatchers.IO) {
            _savePasswordDataLiveData.value = (LoadingState(isLoading = true))
            try {
                val websiteIcon = repository.fetchwebsiteIcon(websiteUrl)
                val icon: String? = websiteIcon.icons[3]?.url
                val base64 = encodeToString(icon?.encodeToByteArray(), Base64.DEFAULT)
                val fortressModel = FortressModel(
                    websiteUrl,
                    username, password, base64
                )
                val passwordEntity = PasswordEntity(
                    null,
                    websiteName = websiteName,
                    website = websiteUrl, otherInfo = buzzWord,
                    iconBytes = base64
                )
                passwordEntity.fortressModel = fortressModel
                repository.savePassword(cipher, passwordEntity)
                _savePasswordDataLiveData.postValue(LoadingState(data = true))
            }catch (e : Exception){
                e.printStackTrace()
                _savePasswordDataLiveData.postValue(LoadingState(error = e.message))
            }
        }
    }
}