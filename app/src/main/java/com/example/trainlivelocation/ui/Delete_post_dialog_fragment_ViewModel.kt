package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.PostModelResponse
import com.example.domain.entity.StationResponseItem
import com.example.domain.entity.userResponseItem
import com.example.domain.usecase.DeletePostWithID
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class Delete_post_dialog_fragment_ViewModel @Inject constructor(
    private val deletePostWithID: DeletePostWithID,
    private val context: Context
):ViewModel() {

    var btnYesClicked = SingleLiveEvent<Boolean>()
    var btnNoClicked = SingleLiveEvent<Boolean>()

    private val _postDeleted: MutableLiveData<Resource<PostModelResponse?>> = MutableLiveData(null)
    val postDeleted: LiveData<Resource<PostModelResponse?>> = _postDeleted

    fun onbtnYesClicked(view: View){
        btnYesClicked.postValue(true)
    }

    fun onbtnNoClicked(view: View){
        btnNoClicked.postValue(true)
    }

    fun deletePostFromApi(postId:Int?){
        _postDeleted.value=Resource.Loading
        viewModelScope.launch {
            deletePostWithID(postId){
                _postDeleted.value=it
            }
        }
    }


}