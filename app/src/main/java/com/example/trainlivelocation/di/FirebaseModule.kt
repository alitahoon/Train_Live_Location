package com.example.trainlivelocation.di

import com.example.domain.repo.userRepo
import com.example.domain.usecase.AddNewUser
import com.example.domain.usecase.CheckUserAuthFB
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
    fun provideCheckUserAuthFB(UserRepo: userRepo):CheckUserAuthFB{
        return CheckUserAuthFB(UserRepo)
    }

    @Provides
    @Singleton
    fun provideAddNewUser(UserRepo: userRepo):AddNewUser{
        return AddNewUser(UserRepo)
    }
}