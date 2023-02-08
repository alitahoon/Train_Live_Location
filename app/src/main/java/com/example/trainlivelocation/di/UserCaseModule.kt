package com.example.trainlivelocation.di

import com.example.domain.repo.UserRepo
import com.example.domain.usecase.GetUserData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserCaseModule  {

    @Provides
    fun provideUserCaseUserData(UserRepo: UserRepo): GetUserData{
        return GetUserData(UserRepo)
    }
    @Provides
    fun provideUserCaseCheckAuth(UserRepo: UserRepo): CheckUserAuthFB{
        return CheckUserAuthFB(UserRepo)
    }
}
