package com.example.covid19_android

sealed class Resource<out T> {
    class Loading<out T>: Resource<T>()
    data class Success<out T>(val data: T): Resource<T>()
    data class Error<out T>(val exception: Exception): Resource<T>()
}
