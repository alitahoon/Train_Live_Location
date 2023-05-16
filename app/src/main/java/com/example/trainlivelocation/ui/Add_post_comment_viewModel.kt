package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.*
import com.example.domain.usecase.CreatePostComment
import com.example.domain.usecase.GetCommentsForPostUsingId
import com.example.domain.usecase.PushAddPostNotification
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class Add_post_comment_viewModel @Inject constructor(
    private val createPostComment: CreatePostComment,
    private val getCommentsForPostUsingId: GetCommentsForPostUsingId,
    private val context: Context,
) : ViewModel(){

    var btnSendCommentClicked= SingleLiveEvent<Boolean>()
    private val sharedPrefFile = "UserToken"

    private val _uploadComment: MutableLiveData<Resource<CommentResponse>>? = MutableLiveData(null)
    val uploadComment: LiveData<Resource<CommentResponse>>? = _uploadComment

    private val _postComments: MutableLiveData<Resource<ArrayList<PostCommentsResponseItem>>>? = MutableLiveData(null)
    val postComments: LiveData<Resource<ArrayList<PostCommentsResponseItem>>>? = _postComments

    private val _userData: MutableLiveData<UserResponseItem?> = MutableLiveData(null)
    val userData: LiveData<UserResponseItem?> = _userData




    fun sendPostCommentToApi(commentRequest: CommentRequest) {
        _uploadComment!!.value = Resource.Loading
        viewModelScope.launch {
            val child1= launch(Dispatchers.IO) {
                createPostComment(commentRequest) {
                    val child2 = launch(Dispatchers.Main) {
                        _uploadComment!!.value = it  }
                }
            }
            child1.join()
        }
    }

    fun onbtnSendCommentClicked(view: View){
        btnSendCommentClicked.postValue(true)
    }

    fun getPostComments(postId:Int?){
        _postComments!!.value = Resource.Loading
        viewModelScope.launch {
            val child1= launch (Dispatchers.IO) {
                getCommentsForPostUsingId(postId) {
                    val child2 =launch(Dispatchers.Main) {
                        _postComments!!.value = it }

                }
            }
            child1.join()
        }
    }



    fun getUserDataFromsharedPreference() {
        val userSharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        _userData.postValue(
            UserResponseItem(
                userSharedPreferences.getString("userAddress","empty")!!,
                userSharedPreferences.getString("userBirthdate","empty")!!,
                userSharedPreferences.getString("userEmail","empty")!!,
                userSharedPreferences.getString("userGender","empty")!!,
                userSharedPreferences.getInt("userId",0),
                userSharedPreferences.getString("userJop","empty")!!,
                userSharedPreferences.getString("userName","empty")!!,
                userSharedPreferences.getString("userPassword","empty")!!,
                userSharedPreferences.getString("userPhone","empty")!!,
                userSharedPreferences.getString("userRole","empty")!!,
                userSharedPreferences.getInt("userStationId",0)
            )
        )
    }

}