package com.example.trainlivelocation.ui

import Resource
import androidx.appcompat.app.AppCompatActivity
import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.*
import com.example.domain.entity.*
import com.example.domain.usecase.*
import com.example.trainlivelocation.R
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val addNewUser: AddNewUser,
    private val application: Application,
    private val context: Context,
    private val sendImageToFirebaseStorage: SendImageToFirebaseStorage,
    private val createUserNotificationToken: CreateUserNotificationToken
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
    var userAddress: String? = null
    var userBirthDate: String? = null
    var userStation: String? = null
    var joblist = application.resources.getStringArray(R.array.jopsArray)
    var nextBtnClicked = SingleLiveEvent<Boolean>()
    var submitBtnClicked = SingleLiveEvent<Boolean>()
    var chooseImageBtnClicked = SingleLiveEvent<Boolean>()
    var datePickerTxtClicked = SingleLiveEvent<Boolean>()
    var stationTxtClicked = SingleLiveEvent<Boolean>()

    private var _sendingProfileImageResult: MutableLiveData<Resource<String>?> =
        MutableLiveData(null)
    var sendingProfileImageResult: LiveData<Resource<String>?> = _sendingProfileImageResult


    private var _generateNotificationToken: MutableLiveData<Resource<String?>> =
        MutableLiveData(null)
    var generateNotificationToken: LiveData<Resource<String?>> = _generateNotificationToken

    companion object {
        private var layoutCounter: Int? = 0
    }

    fun getLayoutCounter(): Int = layoutCounter!!

    fun onNextBtnClicked(view: View) {
        if (layoutCounter == 0) {
            nextBtnClicked.postValue(true)
            layoutCounter = layoutCounter!!.plus(1)
        } else {
            layoutCounter = 0
            submitBtnClicked.postValue(true)
        }
    }

    fun onDatePickerTxtClicked(view: View) {
        datePickerTxtClicked.postValue(true)
    }

    fun onStationTxtClicked(view: View) {
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

    fun generateUserNotificationToken() {
        _generateNotificationToken.value = Resource.Loading
        viewModelScope.launch {
            createUserNotificationToken() {
                _generateNotificationToken.value = it
            }
        }
    }


    //handel jop spinner listener
    val selectedItemFromSpinner: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(jopSpinnerSelectedPosition) { pos: Int? ->
            value = joblist.get(pos!!)
            Log.d(TAG, "spinner_index" + " " + pos)
        }
    }

    private val _userDataMuta: MutableLiveData<Resource<UserResponseItem>?> = MutableLiveData(null)
    val userDataLive: LiveData<Resource<UserResponseItem>?> = _userDataMuta

    fun sendUserDataToApi(userPhone: String?, stationId: Int?, userToken: String) {
        //Start send user data to api
        _userDataMuta.value = Resource.Loading
        if (userAddress != null || userBirthDate != null || userEmail != null || gender_redio_checked.value != null || selectedJop != null
            || userPassword != null || userPhone != null || userToken != null
        ) {
            val newUser = RegisterUser(
                userAddress!!,
                userBirthDate!!,
                userEmail?.trim()!!,
                gender_redio_checked.value!!,
                selectedJop!!,
                userName?.trim()!!,
                userPassword?.trim()!!,
                userPhone?.trim()!!,
                "normal",
                userToken)
            Log.i(TAG, "${newUser}")
            viewModelScope.launch {
                val child1=launch (Dispatchers.IO){
                    var result = addNewUser(
                        newUser
                    ) {
                        val child2=launch (Dispatchers.Main){
                            _userDataMuta.value = it
                        }
                    }
                }
                child1.join()

            }
        }else{
            _userDataMuta.value=Resource.Failure("Please Enter  All Fields...")
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
        _sendingProfileImageResult.value = Resource.Loading
        viewModelScope.launch {
            sendImageToFirebaseStorage(profileImageUri, "profileImages/${userPhone!!}") {
                Log.i(TAG, "${it}")
                _sendingProfileImageResult.value = it
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

    fun getAddress(longitude: Double, latitude: Double) {
        Log.i(TAG, "${longitude},${latitude}")
        val geocoder: Geocoder
        val addresses: List<Address>?
        geocoder = Geocoder(context, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            longitude,
            latitude,
            4
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        val address: String =
            addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        if (addresses[0] == null) {
            val city: String = addresses!![0].locality
            val state: String = addresses!![0].adminArea
            val country: String = addresses!![0].countryName
            val postalCode: String = addresses!![0].postalCode
            val knownName: String = addresses!![0].featureName // Only if available else return NULL
            userAddress = ""
        } else {
            //get first name of state
            val stateArr = addresses!![0].adminArea.split(" ")
            this.userAddress =
                "${addresses!![0].locality},${stateArr[0]},${addresses!![0].countryName}"
        }


    }


}


