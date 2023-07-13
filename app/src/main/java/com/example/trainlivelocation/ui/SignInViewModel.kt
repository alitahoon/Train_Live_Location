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
import com.example.domain.entity.UserSignInDataEntity
import com.example.domain.usecase.*
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val getUserData: GetUserData,
    private val context: Context,
    private val insertUserSignInDataEntity: InsertUserSignInDataEntity,
    private val getAllUserSignInDataEntity: GetAllUserSignInDataEntity,
    private val clearDirectionRouteFromDatabase: ClearDirectionRouteFromDatabase,
    private val clearStationsFromDatabase: ClearStationsFromDatabase
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

    private val _insertUserSignInData: MutableLiveData<Resource<String>?> =
        MutableLiveData(null)
    val insertUserSignInData: LiveData<Resource<String>?> = _insertUserSignInData

    private val _getUserSignInData: MutableLiveData<Resource<ArrayList<UserSignInDataEntity>>?> =
        MutableLiveData(null)
    val getUserSignInData: LiveData<Resource<ArrayList<UserSignInDataEntity>>?> = _getUserSignInData

    fun clearDirectionRoute(){
        Log.i(TAG,"clearDirectionRoute")
        viewModelScope.launch {
            val child1=launch (Dispatchers.IO){
                clearDirectionRouteFromDatabase(){
                    val child2=launch (Dispatchers.Main){
                        when(it){
                            is Resource.Success->{
                                Log.i(TAG, "${it.data}")
                            }
                            is Resource.Loading->{
                                Log.i(TAG, "Loading")
                            }
                            is Resource.Failure->{
                                Log.e(TAG, "${it.error}")
                            }
                            else->{}
                        }
                    }

                }
            }

        }
    }
    fun clearStationsFromDatabase(){
        Log.i(TAG,"clearDirectionRoute")
        viewModelScope.launch {
            val child1=launch (Dispatchers.IO){
                clearStationsFromDatabase(){
                    val child2=launch (Dispatchers.Main){
                        when(it){
                            is Resource.Success->{
                                Log.i(TAG, "${it.data}")
                            }
                            is Resource.Loading->{
                                Log.i(TAG, "Loading")
                            }
                            is Resource.Failure->{
                                Log.e(TAG, "${it.error}")
                            }
                            else->{}
                        }
                    }

                }
            }

        }
    }
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

    fun cashingUserData(userSignInDataEntity: UserSignInDataEntity){
        viewModelScope.launch {
            val child1 = launch(Dispatchers.IO) {
                insertUserSignInDataEntity(userSignInDataEntity){
                    val child2 = launch(Dispatchers.Main) {
                        _insertUserSignInData.value = it
                    }
                }
            }
            child1.join()
        }
    }

    fun checkingUserData(){
        viewModelScope.launch {
            val child1 = launch(Dispatchers.IO) {
                getAllUserSignInDataEntity{
                    val child2 = launch(Dispatchers.Main) {
                        _getUserSignInData.value = it
                    }
                }
            }
            child1.join()
        }
    }

    fun onSignUpBtnClicked(view: View) {
        Log.e(TAG, "onSignUpBtnClicked")
        signUpBtnClicked.postValue(true)
    }

    fun checkIfUserIsSignIn() {
        _userLoginDataMuta.value=Resource.Loading
        viewModelScope.launch {
            val child1=launch (Dispatchers.IO){
                getUserData(userPhone,userPassword){
                    val child2=launch (Dispatchers.Main){
                        cashingUserData(UserSignInDataEntity(userName = userPhone!!, password = userPassword!!))
                        _userLoginDataMuta.value=it
                    }
                }
            }
            child1.join()

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
        editor.putString("tokenForNotifications",userItem.tokenForNotifications)
        editor.putInt("userId",userItem.id)
        editor.apply()
        editor.commit()
    }




}