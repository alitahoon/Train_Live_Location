package com.example.trainlivelocation.ui

import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.userResponseItem
import com.example.domain.usecase.GetUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val getUserData: GetUserData
) : ViewModel() {
    private val TAG:String?="SignInViewModel"
    var userPhone: String? = null
    var userPassword: String? = null
    var signInListener: SignInListener? = null
    private val _userLoginDataMuta: MutableLiveData<ArrayList<userResponseItem>?> =
        MutableLiveData(null)
    val userLoginDataLive: LiveData<ArrayList<userResponseItem>?> = _userLoginDataMuta
    fun onLoginButtonClick(view: View) {
        signInListener?.onStartLogin()

//        if (userPhone.isNullOrEmpty() || userPassword.isNullOrEmpty()) {
//            //view user error message
//            signInListener?.onLoginFailure("Please type your email && password...")
//            return
//        } else {
//            //get data from repo
//            viewModelScope.launch {
//                var result = getUserData(userPhone,userPassword)
//                if (result.isSuccessful) {
//                    if (result.body() != null) {
//                        _userLoginDataMuta.postValue(result.body())
//                        signInListener?.onSuccessLogin(userPhone!!,userPassword!!)
//                    }
//                } else {
//                    Log.e("Register Error in sendUsersData", result.message())
//                    signInListener?.onLoginFailure(result.message())
//                }
//            }
//            viewModelScope.launch {
//
//            }
//        }
    }
    fun onSignUpBtnClicked(view: View){
        Log.e(TAG,"onSignUpBtnClicked")
        signInListener?.onSignUpBtnClicked()
    }

    @BindingAdapter("scrollTo")
    fun scrollTo(view: ScrollView, viewId: Int) {
        if (viewId == 0) {
            view.scrollTo(0, 0)
            return
        }
        val v = view.findViewById<View>(viewId)
        view.scrollTo(0, v.top)
        v.requestFocus()
    }


}