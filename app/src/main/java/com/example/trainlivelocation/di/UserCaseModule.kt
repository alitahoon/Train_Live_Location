package com.example.trainlivelocation.di

import com.example.domain.repo.UserRepo
import com.example.domain.usecase.*
import com.example.trainlivelocation.ui.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserCaseModule  {

    @Provides
    fun provideUserGetCaseUserData(userRepo: UserRepo): GetUserData{
        return GetUserData(userRepo)
    }
    @Provides
    fun provideAddNewUser(userRepo: UserRepo):AddNewUser {
        return AddNewUser(userRepo)
    }

    @Provides
    fun provideSendOtbToPhone(userRepo: UserRepo):SendOtpToPhone{
        return SendOtpToPhone(userRepo)
    }
    @Provides
    fun provideReSendOtbToPhone(userRepo: UserRepo):ResendOtpCode{
        return ResendOtpCode(userRepo)
    }

    @Provides
    fun provideSignInWithPhoneAuthCredential(userRepo: UserRepo):SignInWithPhoneAuthCredential{
        return SignInWithPhoneAuthCredential(userRepo)
    }




}
