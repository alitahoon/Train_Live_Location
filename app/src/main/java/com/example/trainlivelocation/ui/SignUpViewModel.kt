package com.example.trainlivelocation.ui

import Resource
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val addNewUser: AddNewUser,
    private val application: Application,
    private val sendImageToFirebaseStorage: SendImageToFirebaseStorage
) : ViewModel() {
    private var selectedJop: String? = ""
    private val TAG: String? = "RegisterViewModel"
    lateinit var activity: AppCompatActivity
    val gender_redio_checked = MutableLiveData<String>()
    var userName: String? = null
    val jopSpinnerSelectedPosition =
        MutableLiveData<Int>() // This gets updated once spinner item selection changes
    var userPassword: String? = null
    var userEmail: String? = null
    var userBirthDate: String? = null
    var userStation: String? = null
    var joblist = application.resources.getStringArray(R.array.jopsArray)
    var nextBtnClicked = SingleLiveEvent<Boolean>()
    var submitBtnClicked = SingleLiveEvent<Boolean>()
    var chooseImageBtnClicked = SingleLiveEvent<Boolean>()
    var datePickerTxtClicked = SingleLiveEvent<Boolean>()
    var stationTxtClicked = SingleLiveEvent<Boolean>()

    companion object {
        private var layoutCounter: Int? = 0
    }

    fun getLayoutCounter(): Int = layoutCounter!!

    fun onNextBtnClicked(view: View) {
        if (layoutCounter == 0) {
            nextBtnClicked.postValue(true)
            layoutCounter = layoutCounter!!.plus(1)
        } else {
            submitBtnClicked.postValue(true)
        }
    }

    fun onDatePickerTxtClicked(view: View) {
        datePickerTxtClicked.postValue(true)
    }
    fun onStationTxtClicked(view: View){
        stationTxtClicked.postValue(true)
    }

    fun onChooseImageBtnClicked(view: View) {
        chooseImageBtnClicked.postValue(true)
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

    private val _userDataMuta: MutableLiveData<Resource<userResponseItem>?> = MutableLiveData(null)
    val userDataLive: LiveData<Resource<userResponseItem>?> = _userDataMuta

    fun sendUserDataToApi(userPhone: String?,stationId:Int?) {
        //Start send user data to api
        _userDataMuta.value = Resource.Loading
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
                    "normal",
                    stationId!!
                )
            ) {
                _userDataMuta.value = it
            }

            //End send user data to api
        }
    }

        //handle radio check button

        fun onClickMale() {
            gender_redio_checked.postValue("Male")
            Log.i(TAG, "${gender_redio_checked.value!!}")
        }

        fun onClickFemale() {
            gender_redio_checked.postValue("Female")
            Log.i(TAG, "${gender_redio_checked.value!!}")
        }


        fun uploadProfileImage(profileImageUri: Uri, userPhone: String?) {
            viewModelScope.launch {
                sendImageToFirebaseStorage(profileImageUri, "/profileImages/${userPhone!!}") {
                    Log.i(TAG, "${it}")
                }

            }
        }

        fun incrementLayoutCounter() {
            layoutCounter?.plus(1)
        }

        val clickListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedJop = parent?.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }



}


