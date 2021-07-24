package com.example.myapplication.data

data class LoadingState<D>(val data: D? =null, val error: String? = null, var isLoading: Boolean = true){
    init {
        data?.apply {
            isLoading = false
        }

        error?.apply {
            isLoading = false
        }
    }
}