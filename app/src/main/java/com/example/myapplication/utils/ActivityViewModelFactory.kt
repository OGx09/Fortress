package com.example.myapplication.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.repository.FortressRepository
import java.lang.IllegalArgumentException
import kotlin.coroutines.CoroutineContext

class ActivityViewModelFactory(private val repository: FortressRepository,
                               private val coroutineContext: CoroutineContext
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            MainActivityViewModel(repository = repository, encryptedUtils = EncryptionUtilsImpl(), coroutineContext = coroutineContext) as T
        }else{
            throw IllegalArgumentException("viewmodel not found")
        }
    }
}