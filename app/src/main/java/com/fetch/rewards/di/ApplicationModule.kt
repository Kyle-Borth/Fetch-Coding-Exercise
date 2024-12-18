package com.fetch.rewards.di

import com.fetch.rewards.api.NetworkService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideConverterFactory() : Converter.Factory =
        NetworkService.jsonSerializer.asConverterFactory("application/json".toMediaType())

}