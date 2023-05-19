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
import com.example.domain.usecase.*
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class Add_post_comment_viewModel @Inject constructor(
    private val createPostComment: CreatePostComment,
    private val getCommentsForPostUsingId: GetCommentsForPostUsingId,
    private val pushAddPostCommentNotification: PushAddPostCommentNotification,
    private val context: Context,
    private val getNotificationTokenByUserIDFromApi:GetNotificationTokenByUserIDFromApi
) : ViewModel(){

    var btnSendCommentClicked= SingleLiveEvent<Boolean>()
    private val sharedPrefFile = "UserToken"

    private val _uploadComment: MutableLiveData<Resource<CommentResponse>>? = MutableLiveData(null)
    val uploadComment: LiveData<Resource<CommentResponse>>? = _uploadComment

    private val _postComments: MutableLiveData<Resource<ArrayList<PostCommentsResponseItem>>>? = MutableLiveData(null)
    val postComments: LiveData<Resource<ArrayList<PostCommentsResponseItem>>>? = _postComments



    private val _postCommentNotification: MutableLiveData<Resource<String>> = MutableLiveData(null)
    val postCommentNotification: LiveData<Resource<String>> = _postCommentNotification

    private val _postOwnerNotificationToken: MutableLiveData<Resource<NotificationTokenResponse>> = MutableLiveData(null)
    val postOwnerNotificationToken: LiveData<Resource<NotificationTokenResponse>> = _postOwnerNotificationToken




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
    fun getPostOwnerNotificationToken(userID:Int){
        _postOwnerNotificationToken.value=Resource.Loading
        viewModelScope.launch{
            val child1=launch (Dispatchers.IO){
                getNotificationTokenByUserIDFromApi(userID){
                    val child2=launch (Dispatchers.Main){
                        _postOwnerNotificationToken.value=it
                    }
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

    fun sendPostCommentNotification(notification: PushPostCommentNotification){
        _postCommentNotification.value=Resource.Loading
        viewModelScope.launch {
            pushAddPostCommentNotification(notification){
                _postCommentNotification.value=it
            }
        }
    }





}