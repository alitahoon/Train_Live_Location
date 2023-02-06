package com.example.trainlivelocation.di

import com.example.domain.repo.userRepo
import com.example.domain.usecase.CheckUserAuthFB
import com.example.domain.usecase.GetUserData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserCaseModule  {

    @Provides
    fun provideUserCaseUserData(UserRepo: userRepo): GetUserData{
        return GetUserData(UserRepo)
    }
    @Provides
    fun provideUserCaseCheckAuth(UserRepo: userRepo): CheckUserAuthFB{
        return CheckUserAuthFB(UserRepo)
    }
}
