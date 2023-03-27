package com.example.trainlivelocation.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Post
import com.example.domain.usecase.GetAllPostsFromAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CriticalPostViewModel @Inject constructor(
    private val getAllPostsFromAPI: GetAllPostsFromAPI
):ViewModel(){
    private val TAG: String = "AllPostsViewModel"
    private val _postMutableData = MutableLiveData<List<Post>>()
    val postLiveData: LiveData<List<Post>> = _postMutableData
    val showProgressBar = MutableLiveData(false)

    init {
        submitPostList()
    }

    private fun submitPostList() {
        viewModelScope.launch {
            var result = getAllPostsFromAPI()
            if (result.isSuccessful) {
                if (result.body() != null) {
                    _postMutableData.postValue(result.body())
                }
            } else {
                Log.e(TAG, result.message())
            }
        }
    }
}