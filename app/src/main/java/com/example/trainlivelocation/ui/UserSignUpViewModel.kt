package com.example.trainlivelocation.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ScrollView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.navigation.fragment.DialogFragmentNavigator
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.userResponseItem
import com.example.domain.usecase.*
import com.example.trainlivelocation.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserSignUpViewModel @Inject constructor(
    private val addNewUser: AddNewUser,
    private val application: Application,
    private val sendOtpToPhone: SendOtpToPhone,
    private val resendOtpCode: ResendOtpCode,
    private val signInWithPhoneAuthCredential: SignInWithPhoneAuthCredential,
    private val sendProfileImageToFirebaseStorage: SendProfileImageToFirebaseStorage
) : ViewModel() {
    private var selectedJop:String?=""
    private var imageRefrence:StorageReference=Firebase.storage.reference
    private var nextCounter:Int?=0
    var codeVerfication: String? = null
    private val TAG: String? = "RegisterViewModel"
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var resendtoken: PhoneAuthProvider.ForceResendingToken
    lateinit var storedverificationId: String
    lateinit var activity: Activity
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var userSignUpListener: SignUpListener
    val gender_redio_checked = MutableLiveData<String>()
    var userName: String? = null
    var userPhone: String? = null
    val jopSpinnerSelectedPosition =
        MutableLiveData<Int>() // This gets updated once spinner item selection changes
    var userPassword: String? = null
    var userEmail: String? = null
    var userBirthDate: String? = null
    var joblist = application.resources.getStringArray(R.array.jopsArray)

//    init {
//        gender_redio_checked.postValue(R.id.male)//def value
//    }

    fun setbaseActivity(baseActivity: Activity) {
        activity = baseActivity
    }


    //handel jop spinner listener
    val selectedItemFromSpinner: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(jopSpinnerSelectedPosition) { pos: Int? ->
            value = joblist.get(pos!!)
            Log.d(TAG, "spinner_index" + " " + pos)
        }
    }

    private val _userDataMuta: MutableLiveData<ArrayList<userResponseItem>?> = MutableLiveData(null)
    val userDataLive: LiveData<ArrayList<userResponseItem>?> = _userDataMuta


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


    fun onBtnRegisterClick(view: View) {
        if (nextCounter != 0){
            //check values
//
//            Log.d(TAG,gender_redio_checked.value.toString())
//            Log.d(TAG,selectedJop!!)

//            fire base auth
            if (userPhone.isNullOrEmpty() || userName.isNullOrEmpty() ||
                userPassword.isNullOrEmpty() || userEmail.isNullOrEmpty() || userBirthDate.isNullOrEmpty()
            ) {
                //return error message
                userSignUpListener?.onFailure("Please complete register data")
                return
            } else {
                sendOtpToUserPhone()
                return
            }
        }else{
            userSignUpListener.nextBtnClicked("register")
            nextCounter = nextCounter!! + 1
        }


    }

    fun onBtnResendCodeClicked(view: View) {
        viewModelScope.launch {
            resendOtpCode(userPhone, activity, auth, callbacks)
        }
    }

    fun onBtnSubmitCodeClicked(view: View) {
        if (codeVerfication.isNullOrEmpty()) {
            Log.e(TAG, "Please Enter code...")
        } else {
            viewModelScope.launch {
                signInWithPhoneAuthCredential(createAPhoneAuthCredential(codeVerfication!!), auth)
                    .addOnCompleteListener(activity) { task ->
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
                            userSignUpListener.onVerficationSuccess()
                        }
                    }
            }
        }
    }

    fun sendOtpToUserPhone(){
        userSignUpListener.onStartSignUp()
        viewModelScope.launch {
            callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "VerificationCompleted:$credential")
                    signInCredential(credential)
                    userSignUpListener.onVerificationCompleted()
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
                    userSignUpListener.onOtbCodeSendToUser()

                }
            }

            PhoneAuthProvider.verifyPhoneNumber(
                sendOtpToPhone(
                    "+20" + userPhone,
                    activity,
                    auth,
                    callbacks
                )
            )
            Log.d(TAG, "----PhoneAuthProvider")


        }
    }

    private fun signInCredential(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            signInWithPhoneAuthCredential.invoke(credential, auth)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        userSignUpListener.onSuccessSignUp()
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

    fun createAPhoneAuthCredential(code: String): PhoneAuthCredential {
        return PhoneAuthProvider.getCredential(storedverificationId, code)
    }




    fun sendUserDataToApi() {
        //Start send user data to api
        viewModelScope.launch {
            try {
                var result = addNewUser(
                    RegisterUser(
                        "******",
                        "******",
                        userEmail?.trim()!!,
                        gender_redio_checked.value!!,
                        selectedJop!!,
                        userName?.trim()!!,
                        userPassword?.trim()!!,
                        userPhone?.trim()!!,
                        "normal"
                    )
                )
                if (result.isSuccessful) {
                    Log.e(TAG, "success")
                    if (result.body() != null) {
                        _userDataMuta.postValue(result.body())
                        userSignUpListener.onSuccessSignUp()
                    }
                } else {
                    Log.e(TAG, result.message())
                    userSignUpListener.onFailure(result.message())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }
        //End send user data to api
    }

    //handle radio check button

     fun onClickMale(){
        gender_redio_checked.postValue("Male")
    }
     fun onClickFemale(){
        gender_redio_checked.postValue("Female")
    }

    fun onbtnBirthdateClicked(){

    }


    //create an interface to get callbacks when user choose date from date picker dialog

    interface onUserChooseDateListener{
        fun onClickSubmit(choosedDate:String)
        fun onClickCancel()
    }

    fun uploadProfileImage(profileImageUri:Uri){
        viewModelScope.launch {
            sendProfileImageToFirebaseStorage(profileImageUri,userPhone!!,
            imageRefrence)

        }
    }



    val clickListener=object :AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedJop = parent?.getItemAtPosition(position) as String
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }


}


