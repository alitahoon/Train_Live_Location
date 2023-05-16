package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.domain.entity.PushPostNotification
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.*
import com.example.trainlivelocation.utli.SingleLiveEvent
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Add_post_fragment_ViewModel @Inject constructor(
    private val sendImageToFirebaseStorage: SendImageToFirebaseStorage,
    private val getUserDataById: GetUserDataById,
    private val createPost: CreatePost,
    private val context: Context,
    private val pushAddPostNotification: PushAddPostNotification,
    private val getNotificationTokenFromFirebase: GetNotificationTokenFromFirebase

) : ViewModel() {
    var IsProgressBarVisible: Boolean = false
    private val _showProgressBar = MutableLiveData(false)
    val showProgressBar: LiveData<Boolean> = _showProgressBar
    private val TAG: String? = "Add_post_fragment_ViewModel"
    var btnSubmitClicked = SingleLiveEvent<Boolean>()
    var btnChooseImageClicked = SingleLiveEvent<Boolean>()
    var btnChooseTrainClicked = SingleLiveEvent<Boolean>()
    val postContent: String? = null
    val post_redio_checked = MutableLiveData<Boolean>()
    val _trainId: String? = null
    private var imageRefrence: StorageReference = Firebase.storage.reference
    private val sharedPrefFile = "UserToken"

    private val _userData: MutableLiveData<UserResponseItem?> = MutableLiveData(null)
    val userData: LiveData<UserResponseItem?> = _userData


    private val _post: MutableLiveData<Resource<PostModelResponse>>? = MutableLiveData(null)
    val post: LiveData<Resource<PostModelResponse>>? = _post

    private val _sendPostImageToFirebase: MutableLiveData<Resource<String>>? = MutableLiveData(null)
    val sendPostImageToFirebase: LiveData<Resource<String>>? = _sendPostImageToFirebase


    private val _AddedPostNotification: MutableLiveData<Resource<String>> = MutableLiveData(null)
    val AddedPostNotification: LiveData<Resource<String>>?= _AddedPostNotification

    private val _notificationToken: MutableLiveData<Resource<String>> = MutableLiveData(null)
    val notificationToken: LiveData<Resource<String>> = _notificationToken


    fun setCritical() {
        post_redio_checked.postValue(true)
    }

    fun setNONCritical() {
        post_redio_checked.postValue(false)
    }

    fun onSubmitButtonClick() {
        btnSubmitClicked.postValue(true)
    }

    fun onBtnChooseImageclicked(view: View) {
        btnChooseImageClicked.postValue(true)
    }
    fun onBtnChooseTrainclicked(view: View) {
        btnChooseTrainClicked.postValue(true)
    }

    fun sendPostImageToFirebase(postImageUri: Uri, imagePath: String) {
        _sendPostImageToFirebase!!.value = Resource.Loading
        viewModelScope.launch {
            sendImageToFirebaseStorage(postImageUri, imagePath) {
                _sendPostImageToFirebase.value = it
            }
        }
    }

    fun sendAddedPostNotificationPost(notification: PushPostNotification){
        _AddedPostNotification.value=Resource.Loading
        viewModelScope.launch {
            pushAddPostNotification(notification){
                _AddedPostNotification.value=it
            }
        }
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            Log.i(TAG, "${post}")
            _post!!.value = Resource.Loading
            val child1=  launch  (Dispatchers.IO){
                createPost(post) {
                    val child2=   launch(Dispatchers.Main) {
                        _post!!.value = it }

                }
            }
            child1.join()  }
    }

    fun getUserDataFromsharedPreference() {
        val userSharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        _userData.postValue(
            UserResponseItem(
                userSharedPreferences.getString("userAddress", "empty")!!,
                userSharedPreferences.getString("userBirthdate", "empty")!!,
                userSharedPreferences.getString("userEmail", "empty")!!,
                userSharedPreferences.getString("userGender", "empty")!!,
                userSharedPreferences.getInt("userId", 0),
                userSharedPreferences.getString("userJop", "empty")!!,
                userSharedPreferences.getString("userName", "empty")!!,
                userSharedPreferences.getString("userPassword", "empty")!!,
                userSharedPreferences.getString("userPhone", "empty")!!,
                userSharedPreferences.getString("userRole", "empty")!!,
                userSharedPreferences.getInt("userStationId", 0)
            )
        )
    }

    fun getToken(userPhone:String?){
        _notificationToken.value=Resource.Loading
        viewModelScope.launch {
            getNotificationTokenFromFirebase(userPhone){
                _notificationToken.value=it
            }
        }
    }

}