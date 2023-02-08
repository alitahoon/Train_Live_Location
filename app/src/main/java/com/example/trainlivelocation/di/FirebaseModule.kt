package com.example.trainlivelocation.di

import com.example.domain.repo.UserRepo
import com.example.domain.usecase.AddNewUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideCheckUserAuthFB(UserRepo: UserRepo):CheckUserAuthFB{
        return CheckUserAuthFB(UserRepo)
    }

    @Provides
    @Singleton
    fun provideAddNewUser(UserRepo: UserRepo):AddNewUser{
        return AddNewUser(UserRepo)
    }
}