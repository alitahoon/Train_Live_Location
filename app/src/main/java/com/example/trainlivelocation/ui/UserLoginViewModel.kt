package com.example.trainlivelocation.ui

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.GetUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val getUserData:GetUserData
):ViewModel(){

    var userPhone:String? = null
    var userPassword:String? =null
    var loginListener: LoginListener?=null
    fun onLoginButtonClick(view: View){
        loginListener?.onStartLogin()

        if (userPhone.isNullOrEmpty()||userPassword.isNullOrEmpty()){
            //view user error message
            loginListener?.onLoginFailure("Please type your email && password...")
            return
        }else{
            //get data from repo
            loginListener?.onSuccessLogin()
        }
    }


}