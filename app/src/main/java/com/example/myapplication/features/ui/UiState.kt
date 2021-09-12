package com.example.myapplication.features.ui

import com.example.myapplication.data.Result

var hasBeenCalled = false
data class UiState<T> (var isLoading: Boolean = false, var error: String? = null, var data: T? =null){
    val hasError : Boolean get() = error != null

    val initialLoad: Boolean get() = data == null && isLoading && !hasError

    init {
        if ((error != null || data != null)) {
            isLoading = false
        }
    }
}



/*
Copy a state based on result
 */
fun<T> UiState<T>.copyWithResult(result: Result<T>): UiState<T>{
    return when(result){
        is Result.Success -> copy(isLoading = false, error = null, data = result.data)
        is Result.Error -> copy(isLoading = false, error = result.error, data = null)
    }
}