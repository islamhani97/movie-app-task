package com.islam97.android.apps.movie.core.utils

sealed class Result<out T> {
    data class Success<T>(val data: T?) : Result<T>()
    data class Error(val errorMessage: String) : Result<Nothing>()
}