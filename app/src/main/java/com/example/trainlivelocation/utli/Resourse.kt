package com.example.trainlivelocation.Utli

sealed class Resourse<out T> {

    object Loading : Resourse<Nothing>()
    data class Failure(val error: String?): Resourse<Nothing>()
    data class Success<out T>(val data: T): Resourse<T>()
}