package com.example.trainlivelocation.ui

import Resource
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.domain.usecase.GetAllPostsFromAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CriticalPostViewModel @Inject constructor(
    private val getAllPostsFromAPI: GetAllPostsFromAPI
):ViewModel(){
    private val TAG: String = "CriticalPostViewModel"
    private val _postMutableData = MutableLiveData<List<Post>>()
//    private val _postLiveData: MutableLiveData<Resource<List<Post>>> = MutableLiveData(null)
//    val postLiveData: LiveData<Resource<List<Post>>> = _postLiveData
    val showProgressBar = MutableLiveData(false)


    private val _criticalPosts:MutableLiveData<Resource<ArrayList<PostModelResponse>>>?= MutableLiveData(null)
    val criticalPosts:LiveData<Resource<ArrayList<PostModelResponse>>>?= _criticalPosts

    init {
        getPosts()
    }
    fun getPosts() {
        viewModelScope.launch {
            _criticalPosts!!.value = Resource.Loading
            val child1=  launch(Dispatchers.IO) {
                getAllPostsFromAPI {
                    launch (Dispatchers.Main){
                        _criticalPosts.value = it
                    }
                }
            }
        }
    }

//    init {
//        submitPostList()
//    }
//
//    private fun submitPostList() {
//        viewModelScope.launch {
//            var result = getAllPostsFromAPI()
//            if (result.isSuccessful) {
//                if (result.body() != null) {
//                    _postMutableData.postValue(result.body())
//                }
//            } else {
//                Log.e(TAG, result.message())
//            }
//        }
//    }
}