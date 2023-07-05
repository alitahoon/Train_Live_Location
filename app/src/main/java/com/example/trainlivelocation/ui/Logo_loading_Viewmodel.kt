package com.example.trainlivelocation.ui

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.UserResponseItem
import com.example.domain.entity.UserSignInDataEntity
import com.example.domain.usecase.GetAllUserSignInDataEntity
import com.example.domain.usecase.GetUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class Logo_loading_Viewmodel @Inject constructor(
    private val getUserData: GetUserData,
    private val getAllUserSignInDataEntity: GetAllUserSignInDataEntity
) :ViewModel(){

    private val _getUserSignInData: MutableLiveData<Resource<ArrayList<UserSignInDataEntity>>?> =
        MutableLiveData(null)
    val getUserSignInData: LiveData<Resource<ArrayList<UserSignInDataEntity>>?> = _getUserSignInData

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
    fun checkIfUserIsSignIn(userPhone: String, userPassword: String) {
        _userLoginDataMuta.value=Resource.Loading
        viewModelScope.launch {
            getUserData(userPhone,userPassword){
                _userLoginDataMuta.value=it
            }
        }
    }
    private val _userLoginDataMuta: MutableLiveData<Resource<UserResponseItem>?> =
        MutableLiveData(null)
    val userLoginDataLive: LiveData<Resource<UserResponseItem>?> = _userLoginDataMuta

}