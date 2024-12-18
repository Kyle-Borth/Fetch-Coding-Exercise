package com.fetch.rewards.api

import okhttp3.ResponseBody

sealed class WrappedResponse<T> {

    data class SuccessWithBody<T>(val body: T, val responseCode: Int = 200) : WrappedResponse<T>()

    data class SuccessNoBody<T>(val responseCode: Int = 204) : WrappedResponse<T>()

    data class LocalError<T>(val throwable: Throwable) : WrappedResponse<T>()

    data class NetworkError<T>(
        val body: ResponseBody?,
        val throwable: Throwable?,
        val responseCode: Int
    ) : WrappedResponse<T>()

}