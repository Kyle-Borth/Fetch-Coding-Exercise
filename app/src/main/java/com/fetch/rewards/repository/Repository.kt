package com.fetch.rewards.repository

import android.util.Log
import com.fetch.rewards.api.WrappedResponse

private const val TAG = "Repo"

abstract class Repository {

    protected fun <T> WrappedResponse<T>.requireResponseBody(onSuccess: (T) -> Unit, onFailureLogMessage: String) {
        when(this) {
            is WrappedResponse.SuccessWithBody -> onSuccess(this.body)
            is WrappedResponse.SuccessNoBody -> Log.e(TAG, "$onFailureLogMessage: No Response Body")
            is WrappedResponse.NetworkError -> {
                Log.e(TAG, "$onFailureLogMessage: Network Error ${this.responseCode}", this.throwable)
            }
            is WrappedResponse.LocalError -> Log.e(TAG, "$onFailureLogMessage: Local Error", this.throwable)
        }
    }

}