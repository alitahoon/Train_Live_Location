package com.example.trainlivelocation.ui

import androidx.appcompat.app.AppCompatActivity
import android.app.Application
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ScrollView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.userResponseItem
import com.example.domain.usecase.*
import com.example.trainlivelocation.R
import com.example.trainlivelocation.utli.SignUpListener
import com.example.trainlivelocation.utli.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val addNewUser: AddNewUser,
    private val application: Application,
    private val sendImageToFirebaseStorage: SendImageToFirebaseStorage
) : ViewModel() {
    private var selectedJop:String?=""
    private val TAG: String? = "RegisterViewModel"
    lateinit var activity: AppCompatActivity
    val gender_redio_checked = MutableLiveData<String>()
    var userName: String? = null
    val jopSpinnerSelectedPosition =
        MutableLiveData<Int>() // This gets updated once spinner item selection changes
    var userPassword: String? = null
    var userEmail: String? = null
    var userBirthDate: String? = null
    var joblist = application.resources.getStringArray(R.array.jopsArray)
    var nextBtnClicked= SingleLiveEvent<Boolean>()
    var datePickerTxtClicked= SingleLiveEvent<Boolean>()
    var layoutCounter:Int?=0


    fun onNextBtnClicked(view: View){
        nextBtnClicked.postValue(true)
    }
    fun onDatePickerTxtClicked(view: View){
        datePickerTxtClicked.postValue(true)
    }
    init {
        gender_redio_checked.postValue("Male")//def value
    }

    fun setbaseActivity(baseActivity: AppCompatActivity) {
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

    fun sendUserDataToApi(userPhone:String?) {
        //Start send user data to api
        viewModelScope.launch {
                var result = addNewUser(
                    RegisterUser(
                        "******",
                        userBirthDate!!,
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
                    }
                } else {
                    Log.e(TAG,"Failed to send user data to API${result.message()}")
                }

        }
        //End send user data to api
    }

    //handle radio check button

     fun onClickMale(){
        gender_redio_checked.postValue("Male")
         Log.i(TAG,"${gender_redio_checked.value!!}")
    }
     fun onClickFemale(){
        gender_redio_checked.postValue("Female")
         Log.i(TAG,"${gender_redio_checked.value!!}")
     }


    fun uploadProfileImage(profileImageUri:Uri,userPhone: String?){
        viewModelScope.launch {
            sendImageToFirebaseStorage(profileImageUri,"/profileImages/${userPhone!!}"){

            }

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


