package com.example.trainlivelocation.di

import com.example.domain.repo.LocationListener
import android.content.Context
import com.example.data.ApiService
import com.example.data.LocationLive
import com.example.domain.repo.UserRepo
import com.example.repo.userRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    fun ProvideRepo(apiService: ApiService,locationLive: LocationLive):UserRepo{
        return userRepoImpl(apiService,locationLive)
    }

}