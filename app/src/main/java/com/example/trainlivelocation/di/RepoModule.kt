package com.example.trainlivelocation.di

import com.example.data.ApiService
import com.example.data.FirebaseService
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
    fun ProvideRepo(apiService: ApiService,firebaseService: FirebaseService):UserRepo{
        return userRepoImpl(apiService,firebaseService)
    }
}