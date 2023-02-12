package com.example.trainlivelocation.ui

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val addNewUser: AddNewUser,
    private val application: Application
//    private val isUserVerified: IsUserVerified,
//    private val resendOtpCode: ResendOtpCode,
//    private val sendOtpToPhone: SendOtpToPhone,
//    private val setVerificationId: SetVerificationId,
//    private val verifyOtpCode: VerifyOtpCode,
) : ViewModel() {
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

    //handel jop spinner listener
    val selectedItemFromSpinner: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(jopSpinnerSelectedPosition) { pos: Int? ->
            value = joblist.get(pos!!)
            Log.d("spinner_index", " " + pos)
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
//            //send user data to api
//            viewModelScope.launch {
//                try {
//                    var result = addNewUser(
//                        RegisterUser(
//                            "shamma",
//                            "1-5-2001",
//                            "alitahoon2886666@gmail.com",
//                            "Male",
//                            "Sabak_Android",
//                            "ali",
//                            "123",
//                            "01234",
//                            "admin"
//                        )
//                    )
//                    if (result.isSuccessful) {
//                        Log.e("RegisterErrorinsendUsersData", "success")
//                        if (result.body() != null) {
//                            _userDataMuta.postValue(result.body())
//                            userRegisterListener.onSuccessRegister()
//                        }
//                    } else {
//                        Log.e("RegisterErrorinsendUsersData", result.message())
//                        userRegisterListener.onFailure(result.message())
//                    }
//                } catch (e: Exception) {
//                    Log.e("onRegisterViewModel->sendUsersData", e.message.toString())
//                }
//            }
//            //end user data to api

            return
        }

    }

}


