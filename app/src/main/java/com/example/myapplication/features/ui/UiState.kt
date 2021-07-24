package com.example.myapplication.features.ui

data class UiState<T> (val isLoading: Boolean = false, val error: String? = null, val data: T? =null){
    val hasError : Boolean get() = error != null

    val initialLoad: Boolean get() = data == null && isLoading && !hasError
}

fun<T> UiState<T>.copyWithResult(result: Result<>){

}