package com.fetch.rewards.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val TAG = "NtwrkSrvs"

abstract class NetworkService {

    protected suspend fun <T> wrapRequest(
        request: suspend () -> Response<T>
    ): WrappedResponse<T> = withContext(Dispatchers.IO) {
        try {
            val response = request()

            if(response.isSuccessful) {
                response.body()?.let { body ->
                    WrappedResponse.SuccessWithBody(body, response.code())
                } ?: WrappedResponse.SuccessNoBody(response.code())
            }
            else {
                WrappedResponse.NetworkError(response.errorBody(), null, response.code())
            }
        }
        catch (ioE: IOException) {
            // May indicate the user does not have an internet connection
            Log.e(TAG, "API Request Exception", ioE)
            WrappedResponse.LocalError(ioE)
        }
        catch (httpE: HttpException) {
            // Error response from the API
            Log.e(TAG, "API Request Exception: ${httpE.message()}", httpE)
            WrappedResponse.NetworkError(null, httpE, httpE.code())
        }
    }

    protected fun buildOkHttpClient(timeoutSeconds: Long = 10L) = OkHttpClient.Builder().apply {
        connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
        readTimeout(timeoutSeconds, TimeUnit.SECONDS)
        writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
    }.build()

    companion object {
        val jsonSerializer by lazy {
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
                coerceInputValues = true
            }
        }
    }

}