package com.example.myapplication.features.main

import android.util.Base64
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.data.SecretDataWrapper
import com.example.myapplication.features.ui.UiState
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.utils.EncryptionUtils
import com.example.myapplication.utils.SingleLiveEvent
import com.example.myapplication.utils.single
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.lang.IllegalArgumentException
import java.nio.charset.Charset
import java.util.concurrent.CountDownLatch
import javax.crypto.Cipher
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

// Created by Gbenga Oladipupo(Devmike01) on 5/16/21.

@HiltViewModel
open class MainActivityViewModel @Inject constructor(private val coroutineContext: CoroutineContext,
                                                     private val encryptedUtils: EncryptionUtils,
                                                     private val repository: FortressRepository): ViewModel(), MainActivityViewStates{

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

    private val _passwordDetails = MutableLiveData<UiState<PasswordEntity>>()
    val passwordDetails : SingleLiveEvent<UiState<PasswordEntity>> = _passwordDetails.single()

    private val _openWelcomeOrPasswordMain = MutableLiveData<UiState<String>>()
    val openWelcomeOrPasswordMain: LiveData<UiState<String>> = _openWelcomeOrPasswordMain

    val countDownLatch = CountDownLatch(1)

    override fun welcome(username: String){
        _welcomeUsername.value = username
    }

    fun checkForExistingLogin(){
        _openWelcomeOrPasswordMain.value = ( UiState(isLoading = true))
        viewModelScope.launch(coroutineContext) {
            repository.fetchUsername().collect {username ->
                username.apply {
                    if (this != null){
                        print("checkForExistingLogin___ $this")
                        _openWelcomeOrPasswordMain.value = ( UiState(data = this, isLoading = false))
                    }else{
                        _openWelcomeOrPasswordMain.value = ( UiState(error = "No username", isLoading = false))
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



    private fun <T> runWithTryCatch( errorLiveData: MutableLiveData<UiState<T>>, content: suspend () -> Unit) {

        try {
            viewModelScope.launch(coroutineContext) {
                content.invoke()

                print("_openWelcomeOrPasswordMain eeSSSe\n")
            }
        }catch (exception: Exception){
            print("_openWelcomeOrPasswordMain ERROReee\n")
            errorLiveData.value = UiState<T>(isLoading = false, error = exception.message)
        }
    }

    private fun runWithTryCatch(errorContent: () -> Unit, error: (String?) -> Unit) {
        try {
            errorContent.invoke()
        }catch (exception: Exception){
            error.invoke(exception.message)
        }
    }

    private fun handleError(errorContent: (Throwable) -> Unit) = CoroutineExceptionHandler {_, exception ->
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

    fun readSavedPassword(detailId: Int, cipher: Cipher?){
        _passwordDetails.value = UiState(isLoading = true)
        viewModelScope.launch(handleError {
            _passwordDetails.value = UiState(error = it.message, isLoading = false)

        }) {
            if (cipher == null){
                throw NullPointerException("Unable to complete your fingerprint authentication")
            }

            val decryptedData = repository.fetchPasswordDetails(detailId, cipher)
            _passwordDetails.value = UiState(data = decryptedData)
        }
    }


    fun readSavedPasswordDetails(){
        viewModelScope.launch{
            _savePasswordEntityLiveData.addSource(repository.fetchAllEncryptedPasswords()) {
                Log.d("FortressRepository", "FortressRepositoryImpl ${it.size}")
                _savePasswordEntityLiveData.value = it
            }
        }
    }



    fun savePassword(websiteUrl: String, websiteName: String,
                     password: String, otherInfo: String,
                     username: String, cipher: Cipher){

        viewModelScope.launch {
            _savePasswordDataLiveData.value = (UiState(isLoading = true))
            try {

                //val websiteIcon = repository.fetchwebsiteIcon(websiteUrl)
                val iconUrl: String = /*websiteIcon.icons[2]?.url ?:*/ "http://"

                val fortressModel = SecretDataWrapper(password,username, otherInfo)

                val gson = Gson()

                val encryptedData = encryptedUtils.encryptSecretInformation(gson.toJson(fortressModel).toByteArray(
                    Charset.forName("UTF-8")), cipher)

                val passwordEntity = PasswordEntity(
                    null,
                    websiteName = websiteName,
                    website = websiteUrl,
                    iconBytes = iconUrl,
                    initializationVector = Base64.encodeToString(encryptedData?.initializationVector, Base64.NO_WRAP)
                )

                passwordEntity.encryptedData = gson.toJson(encryptedData?.cipherdata)

                Log.d("_savePasswordDataLiveData", "HELLO WORLD=> ${encryptedData?.initializationVector}")
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