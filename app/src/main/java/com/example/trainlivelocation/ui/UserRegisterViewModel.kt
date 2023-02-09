package com.example.trainlivelocation.ui

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.*
import com.example.trainlivelocation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val addNewUser: AddNewUser,
    private val isUserVerified: IsUserVerified,
    private val resendOtpCode: ResendOtpCode,
    private val sendOtpToPhone: SendOtpToPhone,
    private val setVerificationId: SetVerificationId,
    private val verifyOtpCode: VerifyOtpCode,
    private val jobsList:ArrayList< out String>
):ViewModel(){
    lateinit var userRegisterListener:RegisterListener
    lateinit var listenerCallbacks:PhoneCallbacksListener
    val gender_redio_checked=MutableLiveData<Int>()
    var userName:String?=null
    var userPhone:String?=null
    val jopSpinnerSelectedPosition = MutableLiveData<Int>() // This gets updated once spinner item selection changes
    var userPassword:String?=null
    var userEmail:String?=null
    var userBirthDate:String?=null
    var joblist=jobsList

    init {
        gender_redio_checked.postValue(R.id.male)//def value
    }

    //handel jop spinner listener
    val selectedItemFromSpinner: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(jopSpinnerSelectedPosition) { pos: Int? ->
            value= joblist.get(pos!!)
            Log.d("spinner_index"," "+pos )
        }
    }

    fun onBtnRegisterClick(view: View){
        userRegisterListener?.onStartRegister()
        if (userPhone.isNullOrEmpty()||userName.isNullOrEmpty()||
            userPassword.isNullOrEmpty()||userEmail.isNullOrEmpty()||userBirthDate.isNullOrEmpty()){
            //return error message
            userRegisterListener?.onFailure("Please complete register data")
            return
        }else{
            //success
            userRegisterListener?.onSuccessRegister()
            return
        }

    }

}