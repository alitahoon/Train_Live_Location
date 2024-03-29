package com.example.trainlivelocation.ui

import Resource
import android.app.Activity
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.SendOtpToPhone
import com.example.domain.usecase.SetFirebaseServiceActivity
import com.example.trainlivelocation.utli.SingleLiveEvent
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.logging.Handler
import java.util.regex.Pattern
import javax.inject.Inject
@HiltViewModel
class AuthPhoneViewmodel @Inject constructor(
    private val sendOtpToPhone: SendOtpToPhone,
    private val setFirebaseServiceActivity: SetFirebaseServiceActivity
) :ViewModel(){
    var userPhone: String? = null
    var sendCodebtn= SingleLiveEvent<Boolean>()
    var onCodeSent= SingleLiveEvent<Boolean>()
    var onAuthFailed= SingleLiveEvent<Boolean>()
    private lateinit var activity:AppCompatActivity


    private val TAG:String?="AuthPhoneViewmodel"

    fun onSendCodebtnClicked(view: View){
        sendCodebtn.postValue(true)
    }




    fun sendOtb(){
        viewModelScope.launch {

            val REG = "r'/^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})\$/'"
            var PATTERN: Pattern = Pattern.compile(REG)
            fun CharSequence.isPhoneNumber() : Boolean = PATTERN.matcher(this).find()
            if (userPhone!!.length==11){
                sendOtpToPhone("+20"+userPhone!!.trim()){
                    when(it){
                        is Resource.Success->{
                            Log.e(TAG,"${it.data}")
                            when(it.data){
                                "onCodeSent"->{
                                    onCodeSent.value=true
                                }
                                "onVerificationCompleted"->{
                                    Log.i(TAG,"onVerificationCompleted")
                                }
                                "onVerificationFailed"->{
                                    onAuthFailed.value=true
                                }
                            }
                        }
                        is Resource.Failure->{
                            Log.e(TAG," ${it.error}")
                            onAuthFailed.value=true
                        }
                        Resource.Loading->{
                            Log.i(TAG,"Waiting for sending code...")
                        }
                        else -> {}
                    }
                }
            }else{
                //ToDo:show message to user that it is not valid phone number
            }
        }
    }

    fun setActivity(activity: AppCompatActivity){
        Log.i(TAG,"setActivity")
        this.activity=activity
    }

    fun setFirbaseActivity(){
        viewModelScope.launch {
            Log.i(TAG,"setFirbaseActivity")
            setFirebaseServiceActivity(activity)
        }
    }





}