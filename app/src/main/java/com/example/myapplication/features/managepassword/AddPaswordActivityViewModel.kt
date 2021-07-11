package com.example.myapplication.features.managepassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.models.FortressModel
import com.example.myapplication.utils.EncryptionUtils
import com.example.myapplication.utils.FingerprintUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import javax.inject.Inject

@HiltViewModel
class AddPaswordActivityViewModel @Inject constructor(private val repository: FortressRepository,
                                                      private val fingerprintUtil: FingerprintUtils): ViewModel(){


    fun savePassword(website: String, websiteName: String,
                     password: String, buzzWord: String,cipher: Cipher){
        val passwordEntity = FortressModel( website,
            null, password, websiteName, buzzWord)
        viewModelScope.launch {
            repository.savePassword(cipher, passwordEntity)
        }
    }
}