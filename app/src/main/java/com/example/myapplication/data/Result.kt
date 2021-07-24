package com.example.myapplication.data

sealed class Result<out R> {

    /**
     * A generic class that holds a value or an exception
     */
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val error: String?): Result<Nothing>()
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeed get() = this is Result.Success && data != null

fun<T> Result<T>.successOr(fallback: T) = (this as? Result.Success<T>)?.data ?: fallback