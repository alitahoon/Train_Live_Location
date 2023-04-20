package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.GetUserData
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val getUserData: GetUserData,
    private val context: Context
) : ViewModel() {
    private val TAG: String? = "SignInViewModel"
    var userPhone: String? = ""
    var userPassword: String? = ""
    var signInBtnClicked = SingleLiveEvent<Boolean>()
    var signUpBtnClicked = SingleLiveEvent<Boolean>()
    var PhoneNumberIsNotCorrect = SingleLiveEvent<Boolean>()
    var loginfialed = SingleLiveEvent<Boolean>()

    private val _userLoginDataMuta: MutableLiveData<Resource<UserResponseItem>?> =
        MutableLiveData(null)
    val userLoginDataLive: LiveData<Resource<UserResponseItem>?> = _userLoginDataMuta


    fun onSignInBtnClicked(view: View) {
        val REG = "r'/^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})\$/'"
        var PATTERN: Pattern = Pattern.compile(REG)
        fun CharSequence.isPhoneNumber() : Boolean = PATTERN.matcher(this).find()
        if (userPhone!!.length==11){
            signInBtnClicked.postValue(true)
        }else{
            PhoneNumberIsNotCorrect.postValue(true)
        }
    }

    fun onSignUpBtnClicked(view: View) {
        Log.e(TAG, "onSignUpBtnClicked")
        signUpBtnClicked.postValue(true)
    }

    fun checkIfUserIsSignIn() {
        _userLoginDataMuta.value=Resource.Loading
        viewModelScope.launch {
            getUserData(userPhone,userPassword){
                _userLoginDataMuta.value=it
            }
        }
    }

    fun saveUserTokenInSharedPreferences(userItem: UserResponseItem){
        val userSharedPreferences :SharedPreferences=context.getSharedPreferences("UserToken",Context.MODE_PRIVATE)
        var editor=userSharedPreferences.edit()
        editor.putString("userName",userItem.name)
        editor.putString("userPhone",userItem.phone)
        editor.putString("userBirthdate",userItem.birthDate)
        editor.putString("userAddress",userItem.address)
        editor.putString("userJop",userItem.jop)
        editor.putString("userEmail",userItem.email)
        editor.putString("userRole",userItem.role)
        editor.putString("userGender",userItem.gender)
        editor.putString("userPassword",userItem.password)
        editor.putInt("userStationId",userItem.stationId)
        editor.putInt("userId",userItem.id)
        editor.apply()
        editor.commit()
    }




}