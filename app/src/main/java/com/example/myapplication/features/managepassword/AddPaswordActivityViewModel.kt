package com.example.myapplication.features.managepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.features.repository.FortressRepository
import com.example.myapplication.features.repository.database.PasswordEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPaswordActivityViewModel @Inject constructor(private val repository: FortressRepository): ViewModel(){


    fun savePassword(website: String, websiteName: String,
                     password: String, buzzWord: String){
        val passwordEntity = PasswordEntity(0, website,
            null, password, websiteName, buzzWord)
        viewModelScope.launch {
            repository.savePassword(passwordEntity)
        }
    }
}