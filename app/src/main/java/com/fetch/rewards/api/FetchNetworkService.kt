package com.fetch.rewards.api

import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"

@Singleton
class FetchNetworkService @Inject constructor(converterFactory: Converter.Factory) : NetworkService() {

    private val fetchApi: FetchApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildOkHttpClient())
            .addConverterFactory(converterFactory)
            .build()
            .create(FetchApi::class.java)
    }

    suspend fun getFetchItems() = wrapRequest { fetchApi.getFetchItems() }

}