package com.example.trainlivelocation.ui

import com.example.domain.usecase.GetUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val getUserData:GetUserData
){


}