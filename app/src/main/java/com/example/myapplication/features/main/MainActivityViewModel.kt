package com.example.myapplication.features.main

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.data.FortressModel
import com.example.myapplication.data.LoadingState
import com.example.myapplication.features.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import javax.inject.Inject

// Created by Gbenga Oladipupo(Devmike01) on 5/16/21. /storage/emulated/0/Android/data/com.appzonegroup.fcmb.dev/files/Pictures/JPEG_20210718_183510_3308039024051686293.jpg

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: FortressRepository): ViewModel(), MainActivityViewStates{

    private val _savePasswordEntityLiveData = MediatorLiveData<List<PasswordEntity>>()
    val savePasswordEntityLiveData : MediatorLiveData<List<PasswordEntity>> = _savePasswordEntityLiveData

    private val _savePasswordDataLiveData = MutableLiveData<LoadingState<Boolean>>()
    val savePasswordDataLiveData : LiveData<LoadingState<Boolean>> = _savePasswordDataLiveData


    private val _messageState = MutableSharedFlow<String>(replay =1, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val messageState: SharedFlow<String> = _messageState.asSharedFlow()

    private val _welcomeUsername = MutableLiveData<String>()
    val welcomeUsername: LiveData<String> = _welcomeUsername

    private val _openPasswordMain = MutableSharedFlow<UiState<String>>(replay =1, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val openPasswordMain: SharedFlow<UiState<String>> = _openPasswordMain.asSharedFlow()

    init {
        readSavedPasswordDetails()
    }

    override fun welcome(username: String){
        _welcomeUsername.value = username
    }

    override fun saveWelcomeUsername(username: String) {
        viewModelScope.launch {
            repository.saveToDataStore(username)
        }
    }

    fun showMessage(msg: String){
        _messageState.tryEmit(msg)
    }


    fun openPasswordMain(username : String?) {
        if (username != null && username.length > 2) {
            //Save to the data store and proceed!
            viewModelScope.launch {
                repository.saveToDataStore(username)
                _openPasswordMain.tryEmit(UiState(data = username))
            }
        } else {
            _openPasswordMain.tryEmit(UiState(error = "Your username length must be more than two"))
        }
    }


    fun readSavedPassword(cipher: Cipher, id: Int){
        viewModelScope.launch(Dispatchers.Main) {
            val allPasswords = repository.fetchPasswordDetails(cipher = cipher, id =id)
            allPasswords?.apply {
              //  _savePasswordDataLiveData.postValue( this)
            }
        }
    }


    private fun readSavedPasswordDetails(){
        viewModelScope.launch{
            _savePasswordEntityLiveData.addSource(repository.fetchAllEncryptedPasswords()) {
                Log.d("FortressRepository", "FortressRepositoryImpl ${it.size}")
                _savePasswordEntityLiveData.value = it
            }
        }
    }



    fun savePassword(websiteUrl: String, websiteName: String,
                     password: String, buzzWord: String,
                     username: String, cipher: Cipher){

        viewModelScope.launch {
            _savePasswordDataLiveData.value = (LoadingState(isLoading = true))
            try {
                val websiteIcon = repository.fetchwebsiteIcon(websiteUrl)
                val iconUrl: String? = websiteIcon.icons[2]?.url

                val fortressModel = FortressModel(
                    websiteUrl,
                    username, password, iconUrl
                )
                val passwordEntity = PasswordEntity(
                    null,
                    websiteName = websiteName,
                    website = websiteUrl, otherInfo = buzzWord,
                    iconBytes = iconUrl ?: ""
                )
                passwordEntity.fortressModel = fortressModel
                repository.savePassword(cipher, passwordEntity)
                _savePasswordDataLiveData.value = LoadingState(data = true)
            }catch (e : Exception){
                e.printStackTrace()
                Log.e("MyException", "${e.stackTrace}")
                _savePasswordDataLiveData.value = LoadingState(error = e.message)
            }
        }
    }
}