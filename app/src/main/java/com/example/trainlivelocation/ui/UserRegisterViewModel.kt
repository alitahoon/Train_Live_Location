package com.example.trainlivelocation.ui

import android.app.Activity
import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import com.example.domain.usecase.*
import com.example.trainlivelocation.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.Objects
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val addNewUser: AddNewUser,
    private val application: Application,
    private val sendOtpToPhone: SendOtpToPhone,
    private val resendOtpCode: ResendOtpCode,
    private val signInWithPhoneAuthCredential: SignInWithPhoneAuthCredential
//    private val isUserVerified: IsUserVerified,
//    private val resendOtpCode: ResendOtpCode,
//    private val sendOtpToPhone: SendOtpToPhone,
//    private val setVerificationId: SetVerificationId,
//    private val verifyOtpCode: VerifyOtpCode,
) : ViewModel() {
    private val TAG:String?="RegisterViewModel"
    var auth:FirebaseAuth=FirebaseAuth.getInstance()
    lateinit var resendtoken: PhoneAuthProvider.ForceResendingToken
    lateinit var storedverificationId:String
    lateinit var activity: Activity
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var userRegisterListener: RegisterListener
    lateinit var listenerCallbacks: PhoneCallbacksListener
    val gender_redio_checked = MutableLiveData<Int>()
    var userName: String? = null
    var userPhone: String? = null
    val jopSpinnerSelectedPosition =
        MutableLiveData<Int>() // This gets updated once spinner item selection changes
    var userPassword: String? = null
    var userEmail: String? = null
    var userBirthDate: String? = null
    var joblist = application.resources.getStringArray(R.array.jopsArray)
    init {
        gender_redio_checked.postValue(R.id.male)//def value
    }
    fun setbaseActivity(baseActivity: Activity){
        activity=baseActivity
    }


    //handel jop spinner listener
    val selectedItemFromSpinner: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(jopSpinnerSelectedPosition) { pos: Int? ->
            value = joblist.get(pos!!)
            Log.d(TAG,"spinner_index" +" " + pos)
        }
    }

    private val _userDataMuta: MutableLiveData<ArrayList<userResponseItem>?> = MutableLiveData(null)
    val userDataLive: LiveData<ArrayList<userResponseItem>?> = _userDataMuta

    fun onBtnRegisterClick(view: View) {
        userRegisterListener?.onStartRegister()
        if (userPhone.isNullOrEmpty() || userName.isNullOrEmpty() ||
            userPassword.isNullOrEmpty() || userEmail.isNullOrEmpty() || userBirthDate.isNullOrEmpty()
        ) {
            //return error message
            userRegisterListener?.onFailure("Please complete register data")
            return
        } else {
            sendOtpToUserPhone()
            return
        }

    }

    fun sendOtpToUserPhone() {
        viewModelScope.launch {
            callbacks= object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "VerificationCompleted:$credential")

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.d(TAG, "VerificationFailed:${p0.message}")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // The SMS verification code has been sent to the provided phone number, we
                    Log.d(TAG, "onCodeSent:$verificationId")
                    // Save verification ID and resending token so we can use them later
                    storedverificationId = verificationId
                    resendtoken = token
                    //open code verification fragment
                    userRegisterListener.onOtbCodeSendToUser()

                }
            }
            PhoneAuthProvider.verifyPhoneNumber(sendOtpToPhone("+20"+userPhone,activity,auth,callbacks))
            Log.d(TAG, "----PhoneAuthProvider")


        }
    }
    private fun signInCredential(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            signInWithPhoneAuthCredential.invoke(credential,auth)
                .addOnCompleteListener(activity){ task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")

                        val user = task.result?.user
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                        // Update UI
                    }
                }
        }
    }

    fun CreateAPhoneAuthCredential(code:String):PhoneAuthCredential{
        return PhoneAuthProvider.getCredential(storedverificationId,code)
    }


    fun sendUserDataToApi() {
        //Start send user data to api
        viewModelScope.launch {
            try {
                var result = addNewUser(
                    RegisterUser(
                        "shamma",
                        "1-5-2001",
                        "alitahoon2886666@gmail.com",
                        "Male",
                        "Sabak_Android",
                        "ali",
                        "123",
                        "01234",
                        "admin"
                    )
                )
                if (result.isSuccessful) {
                    Log.e(TAG, "success")
                    if (result.body() != null) {
                        _userDataMuta.postValue(result.body())
                        userRegisterListener.onSuccessRegister()
                    }
                } else {
                    Log.e(TAG, result.message())
                    userRegisterListener.onFailure(result.message())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }
        //End send user data to api
    }


}


