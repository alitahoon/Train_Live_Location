package com.example.trainlivelocation.ui

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.domain.usecase.CreatePost
import com.example.domain.usecase.GetUserDataById
import com.example.domain.usecase.SendImageToFirebaseStorage
import com.example.trainlivelocation.utli.SingleLiveEvent
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Add_post_fragment_ViewModel @Inject constructor(
    private val sendImageToFirebaseStorage: SendImageToFirebaseStorage,
    private val getUserDataById: GetUserDataById,
    private val createPost: CreatePost
) : ViewModel() {
    var IsProgressBarVisible:Boolean = false
    private val _showProgressBar = MutableLiveData(false)
    val showProgressBar: LiveData<Boolean> = _showProgressBar
    private val TAG:String?="Add_post_fragment_ViewModel"
    var btnSubmitClicked=SingleLiveEvent<Boolean>()
    var btnChooseImageClicked= SingleLiveEvent<Boolean>()
    val postContent: String? = null
    val post_redio_checked = MutableLiveData<Boolean>()
    val _trainId:String?=null
    private var imageRefrence:StorageReference= Firebase.storage.reference

    private val _postMutableLiveData:MutableLiveData<PostModelResponse>? = MutableLiveData(null)
    val _postLiveData:LiveData<PostModelResponse>?=_postMutableLiveData


    fun setCritical(){
        post_redio_checked.postValue(true)
    }
    fun setNONCritical(){
        post_redio_checked.postValue(false)
    }
    fun onSubmitButtonClick(){
        btnSubmitClicked.postValue(true)
    }

    fun onBtnChooseImageclicked(view: View){
        btnChooseImageClicked.postValue(true)
    }

    fun sendPostImageToFirebase(postImageUri:Uri,imagePath:String){
        _showProgressBar.postValue(true)
        viewModelScope.launch {
            sendImageToFirebaseStorage(postImageUri,imagePath){

            }
        }
    }


    fun  addPost(post:Post){
        _showProgressBar.postValue(true)
        viewModelScope.launch {
            var result=createPost(post)
            if (result.isSuccessful){
                if (result.body()!=null){
                    Log.e(TAG,result.body().toString())
                    _postMutableLiveData?.postValue(result.body())
                    _showProgressBar.postValue(false)
                }
            }else{
                _showProgressBar.postValue(false)
                Log.e(TAG,result.message())
            }
        }
    }

}