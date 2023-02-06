package com.example.trainlivelocation.di

import com.example.data.ApiService
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Time
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient{
        return OkHttpClient.Builder()
            .connectTimeout(20,TimeUnit.SECONDS)
            .readTimeout(20,TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .baseUrl("")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit):ApiService{
        return retrofit.create(ApiService::class.java)
    }
}