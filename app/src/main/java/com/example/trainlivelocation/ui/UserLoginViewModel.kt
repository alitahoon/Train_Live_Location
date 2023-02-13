package com.example.trainlivelocation.ui

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import com.example.domain.usecase.GetUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val getUserData: GetUserData
) : ViewModel() {

    var userPhone: String? = null
    var userPassword: String? = null
    var loginListener: LoginListener? = null
    private val _userLoginDataMuta: MutableLiveData<ArrayList<userResponseItem>?> =
        MutableLiveData(null)
    val userLoginDataLive: LiveData<ArrayList<userResponseItem>?> = _userLoginDataMuta
    fun onLoginButtonClick(view: View) {
        loginListener?.onStartLogin()

        if (userPhone.isNullOrEmpty() || userPassword.isNullOrEmpty()) {
            //view user error message
            loginListener?.onLoginFailure("Please type your email && password...")
            return
        } else {
            //get data from repo
            viewModelScope.launch {
                var result = getUserData(userPhone,userPassword)
                if (result.isSuccessful) {
                    if (result.body() != null) {
                        _userLoginDataMuta.postValue(result.body())
                        loginListener?.onSuccessLogin()
                    }
                } else {
                    Log.e("Register Error in sendUsersData", result.message())
                    loginListener?.onLoginFailure(result.message())
                }
            }
            viewModelScope.launch {

            }
        }
    }


}