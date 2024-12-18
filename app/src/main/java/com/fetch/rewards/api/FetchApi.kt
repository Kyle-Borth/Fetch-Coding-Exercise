package com.fetch.rewards.api

import com.fetch.rewards.api.model.FetchItem
import retrofit2.Response
import retrofit2.http.GET

interface FetchApi {

    @GET("hiring.json")
    suspend fun getFetchItems() : Response<List<FetchItem>>

}