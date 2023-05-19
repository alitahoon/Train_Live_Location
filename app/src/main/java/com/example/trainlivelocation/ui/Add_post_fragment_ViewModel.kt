package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.domain.entity.*
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
    private val getNotificationTokenFromFirebase: GetNotificationTokenFromFirebase,
    private val getNotificationTokenByUserIDFromApi: GetNotificationTokenByUserIDFromApi,
    private val getNotificationTokenForUsersInTrain: GetNotificationTokenForUsersInTrain


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


    private val _postOwnerNotificationToken: MutableLiveData<Resource<NotificationTokenResponse>> = MutableLiveData(null)
    val postOwnerNotificationToken: LiveData<Resource<NotificationTokenResponse>> = _postOwnerNotificationToken

    private val _usersTokenInTrain: MutableLiveData<Resource<ArrayList<NotificationTokenResponseInTrain>>> = MutableLiveData(null)
    val usersTokenInTrain: LiveData<Resource<ArrayList<NotificationTokenResponseInTrain>>> = _usersTokenInTrain

    private val _post: MutableLiveData<Resource<PostModelResponse>>? = MutableLiveData(null)
    val post: LiveData<Resource<PostModelResponse>>? = _post

    private val _sendPostImageToFirebase: MutableLiveData<Resource<String>>? = MutableLiveData(null)
    val sendPostImageToFirebase: LiveData<Resource<String>>? = _sendPostImageToFirebase


    private val _AddedPostNotification: MutableLiveData<Resource<String>> = MutableLiveData(null)
    val AddedPostNotification: LiveData<Resource<String>>?= _AddedPostNotification




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


    fun getUsersTokenInTrainById(trainID:Int){
        _usersTokenInTrain.value=Resource.Loading
        viewModelScope.launch {
            val child1= launch (Dispatchers.IO){
                getNotificationTokenForUsersInTrain(trainID){
                    val child2=launch (Dispatchers.Main){
                        _usersTokenInTrain.value=it
                    }
                }
            }
            child1.join()
        }
    }





}