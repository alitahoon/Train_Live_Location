package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.CommentRequest
import com.example.domain.entity.CommentResponse
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.userResponseItem
import com.example.domain.usecase.CreatePostComment
import com.example.domain.usecase.GetCommentsForPostUsingId
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class Add_post_comment_viewModel @Inject constructor(
    private val createPostComment: CreatePostComment,
    private val getCommentsForPostUsingId: GetCommentsForPostUsingId,
    private val context: Context
) : ViewModel(){

    var btnSendCommentClicked= SingleLiveEvent<Boolean>()
    private val sharedPrefFile = "UserToken"

    private val _uploadComment: MutableLiveData<Resource<CommentResponse>>? = MutableLiveData(null)
    val uploadComment: LiveData<Resource<CommentResponse>>? = _uploadComment

    private val _postComments: MutableLiveData<Resource<ArrayList<PostCommentsResponseItem>>>? = MutableLiveData(null)
    val postComments: LiveData<Resource<ArrayList<PostCommentsResponseItem>>>? = _postComments

    private val _userData: MutableLiveData<userResponseItem?> = MutableLiveData(null)
    val userData: LiveData<userResponseItem?> = _userData


    fun sendPostCommentToApi(commentRequest: CommentRequest){
        _uploadComment!!.value=Resource.Loading
        viewModelScope.launch {
            createPostComment(commentRequest){
                _uploadComment!!.value=it
            }
        }
    }

    fun onbtnSendCommentClicked(view: View){
        btnSendCommentClicked.postValue(true)
    }

    fun getPostComments(postId:Int?){
        _postComments!!.value=Resource.Loading
        viewModelScope.launch {
            getCommentsForPostUsingId(postId){
                _postComments!!.value=it
            }
        }
    }

    fun getUserDataFromsharedPreference() {
        val userSharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        _userData.postValue(
            userResponseItem(
                userSharedPreferences.getString("address","empty")!!,
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