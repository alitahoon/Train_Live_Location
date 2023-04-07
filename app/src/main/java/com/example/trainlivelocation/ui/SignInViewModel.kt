package com.example.trainlivelocation.ui

import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.userResponseItem
import com.example.domain.usecase.GetUserData
import com.example.trainlivelocation.utli.SignInListener
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val getUserData: GetUserData
) : ViewModel() {
    private val TAG: String? = "SignInViewModel"
    var userPhone: String? = null
    var userPassword: String? = null
    var signInListener: SignInListener? = null
    var signInBtnClicked = SingleLiveEvent<Boolean>()
    var signUpBtnClicked = SingleLiveEvent<Boolean>()
    var loginfialed = SingleLiveEvent<Boolean>()

    private val _userLoginDataMuta: MutableLiveData<userResponseItem?> =
        MutableLiveData(null)
    val userLoginDataLive: LiveData<userResponseItem?> = _userLoginDataMuta
    fun onLoginButtonClick(view: View) {

        if (userPhone.isNullOrEmpty() || userPassword.isNullOrEmpty()) {
            //view user error message
            signInListener?.onLoginFailure("Please type your email && password...")
            return
        } else {
            //get data from repo

        }
    }

    fun onSignUpBtnClicked(view: View) {
        Log.e(TAG, "onSignUpBtnClicked")
        signUpBtnClicked.postValue(true)
    }

    fun checkIfUserIsSignIn(phone: String?, password: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            var result = getUserData(phone, password)
            if (result.isSuccessful) {
                if (result.body() != null) {
                    _userLoginDataMuta.postValue(result.body())
                }
            } else {
                Log.e("Register Error in sendUsersData", result.message())
            }
        }
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