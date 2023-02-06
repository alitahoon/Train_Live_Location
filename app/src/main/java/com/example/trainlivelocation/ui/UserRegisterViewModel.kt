package com.example.trainlivelocation.ui

import com.example.domain.usecase.CheckUserAuthFB
import com.example.domain.usecase.AddNewUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val CheckUserAuthFB: CheckUserAuthFB,
    private val sendUserRegisterData: AddNewUser
){

}