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
    fun provideUserCaseUserData(userRepo: UserRepo): GetUserData{
        return GetUserData(userRepo)
    }
    @Provides
    fun provideAddNewUser(userRepo: UserRepo):AddNewUser{
        return AddNewUser(userRepo)
    }
    @Provides
    fun provideIsUserVerified(userRepo: UserRepo):IsUserVerified{
        return IsUserVerified(userRepo)
    }
    @Provides
    fun provideResendOtbCode(userRepo: UserRepo,@ApplicationContext activity: MainActivity):ResendOtpCode{
        return ResendOtpCode(userRepo,activity)
    }
    @Provides
    fun provideSendOtbCode(userRepo: UserRepo,@ApplicationContext activity: MainActivity):SendOtpToPhone{
        return SendOtpToPhone(userRepo,activity)
    }
    @Provides
    fun provideSetVerificationId(userRepo: UserRepo):SetVerificationId{
        return SetVerificationId(userRepo)
    }
    @Provides
    fun provideVerifyOtbCode(userRepo: UserRepo):VerifyOtpCode{
        return VerifyOtpCode(userRepo)
    }




}
