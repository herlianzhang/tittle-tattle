package com.latihangoding.tittle_tattle.vo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

sealed class Resource<out T> {
    class LOADING<T> : Resource<T>()
    data class ERROR<T>(val e: Exception) : Resource<T>()
    data class SUCCESS<T>(val data: T?) : Resource<T>()
}

fun <T> getResult(call: suspend () -> retrofit2.Response<T>) =
    flow {
        emit(Resource.LOADING<T>())
        try {
            val response = withContext(Dispatchers.IO) { call() }
            if (response.isSuccessful) {
                val body = response.body()
                emit(Resource.SUCCESS(body))
            } else {
                throw Exception(response.message())
            }
        } catch (e: Exception) {
            emit(Resource.ERROR<T>(e))
        }
    }