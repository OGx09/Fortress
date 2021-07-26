package com.example.myapplication.features.main

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.data.FortressModel
import com.example.myapplication.data.LoadingState
import com.example.myapplication.features.ui.UiState
import com.example.myapplication.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.crypto.Cipher
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.Throws

// Created by Gbenga Oladipupo(Devmike01) on 5/16/21. /storage/emulated/0/Android/data/com.appzonegroup.fcmb.dev/files/Pictures/JPEG_20210718_183510_3308039024051686293.jpg



@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: FortressRepository): ViewModel(), MainActivityViewStates{

    private val _savePasswordEntityLiveData = MediatorLiveData<List<PasswordEntity>>()
    val savePasswordEntityLiveData : MediatorLiveData<List<PasswordEntity>> = _savePasswordEntityLiveData

    private val _savePasswordDataLiveData = SingleLiveEvent<UiState<Boolean?>>()
    val savePasswordDataLiveData : SingleLiveEvent<UiState<Boolean?>> = _savePasswordDataLiveData


    private val _messageState = MutableSharedFlow<String>(replay =1, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val messageState: SharedFlow<String> = _messageState.asSharedFlow()

    private val _welcomeUsername = MutableLiveData<String>()
    val welcomeUsername: LiveData<String> = _welcomeUsername

    private val _openPasswordMain = MutableSharedFlow<UiState<String>>(replay =1, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val openPasswordMain: SharedFlow<UiState<String>> = _openPasswordMain.asSharedFlow()

    private val _passwordDetails = MutableLiveData<UiState<FortressModel>>()
    val passwordDetails :LiveData<UiState<FortressModel>> = _passwordDetails

    private val _openWelcomeOrPasswordMain = MutableLiveData<UiState<String>>()
    val openWelcomeOrPasswordMain: LiveData<UiState<String>> = _openWelcomeOrPasswordMain

    init {
        readSavedPasswordDetails()
        checkForExistingLogin()
    }

    override fun welcome(username: String){
        _welcomeUsername.value = username
    }

    private fun checkForExistingLogin(){
        viewModelScope.launch(handleError {
            _openWelcomeOrPasswordMain.value = ( UiState(error = it.message))
        }) {
            repository.fetchUsername().collect {username ->
                username.apply {
                    if (this != null){
                        _openWelcomeOrPasswordMain.value = ( UiState(data = this))
                    }else{
                        throw IllegalArgumentException("No username")
                    }
                }
            }
        }
    }


    override fun saveWelcomeUsername(username: String) {
        viewModelScope.launch {
            repository.saveToDataStore(username)
        }
    }

    fun showMessage(msg: String){
        _messageState.tryEmit(msg)
    }

    private fun handleError(errorContent: (Throwable) -> Unit) = CoroutineExceptionHandler { _, exception ->
        errorContent(exception)
    }

    fun openPasswordMain(username : String?) {
        viewModelScope.launch(handleError{
            _openPasswordMain.tryEmit(UiState(error = it.message))
        }) {
            if (username != null && username.length > 2) {
                //Save to the data store and proceed!
                repository.saveToDataStore(username)
                _openPasswordMain.tryEmit(UiState(data = username))
            } else {
                throw IllegalArgumentException("Your username length must be more than two")
            }
        }

    }


    fun readSavedPassword(cipher: Cipher, id: Int){
        viewModelScope.launch(handleError {
            _passwordDetails.value = UiState(error = it.message)
        }) {
            val allPasswords = repository.fetchPasswordDetails(cipher = cipher, id =id)
            allPasswords?.apply {
                _passwordDetails.value = UiState(data = this)
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
            _savePasswordDataLiveData.value = (UiState(isLoading = true))
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
                _savePasswordDataLiveData.value = UiState(data = true)
            }catch (e : Exception){
                e.printStackTrace()
                Log.e("MyException", "${e.stackTrace}")
                _savePasswordDataLiveData.value = UiState(error = e.message)
            }
        }
    }
}