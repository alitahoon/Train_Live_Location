package com.example.trainlivelocation.ui

import Resource
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.view.PinView
import com.example.domain.usecase.CreateAPhoneAuthCredential
import com.example.domain.usecase.ResendOtpCode
import com.example.domain.usecase.SendOtpToPhone
import com.example.domain.usecase.SignInWithPhoneAuthCredential
import com.example.trainlivelocation.utli.SingleLiveEvent
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AuthCodeViewmodel @Inject constructor(
    private val createAPhoneAuthCredential: CreateAPhoneAuthCredential,
    private val signInWithPhoneAuthCredential: SignInWithPhoneAuthCredential,
    private val resendOtpToPhone: ResendOtpCode
) :ViewModel(){
    private val TAG: String?="AuthCodeViewmodel"
    var btnSubmitClicked= SingleLiveEvent<Boolean>()
    var onCodeSubmitSuccess= SingleLiveEvent<Boolean>()
    var resendCodeClicked= SingleLiveEvent<Boolean>()
    var onCodeSubmitFailer= SingleLiveEvent<Boolean>()
    private lateinit var activity: AppCompatActivity
    private val _resendCode:MutableLiveData<Resource<String>> = MutableLiveData(null)
     val resendCode:LiveData<Resource<String>> = _resendCode


    fun onBtnSubmitClicked(view:View){
        btnSubmitClicked.postValue(true)
    }

    fun createPhoneCredential(code:String?){
        viewModelScope.launch {
            if (code!!.length==6){
                createAPhoneAuthCredential(code!!){
                    signInNewUserWithCredential(it!!)
                }
            }else{
                Log.e(TAG,"Please enter code")
            }
        }
    }


    fun resendOtbCode(phoneNumber:String){
        _resendCode.value=Resource.Loading
        viewModelScope.launch {
            resendOtpToPhone(phoneNumber){
                _resendCode.value=it
            }
        }
    }

    fun onResendBtnClicked(){
        resendCodeClicked.postValue(true)
    }
    fun signInNewUserWithCredential(credential: PhoneAuthCredential){
        viewModelScope.launch {
            signInWithPhoneAuthCredential(credential){
                when(it){
                    "success"->{
                        Log.i(TAG,"signInNewUserWithCredential:success")
                        onCodeSubmitSuccess.postValue(true)
                    }
                    "failure"->{
                        onCodeSubmitFailer.postValue(true)
                    }
                }
            }
        }
    }

    fun setActivity(activity: AppCompatActivity){
        Log.i(TAG,"setActivity")
        this.activity=activity
    }

}