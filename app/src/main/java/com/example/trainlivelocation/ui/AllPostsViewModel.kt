package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.PostModelResponse
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.GetAllPostsFromAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllPostsViewModel @Inject constructor(
    private val getAllPostsFromAPI: GetAllPostsFromAPI,
    private val context: Context
)  : ViewModel() {


    private val _allPosts:MutableLiveData<Resource<ArrayList<PostModelResponse>>>?= MutableLiveData(null)
    val allPosts:LiveData<Resource<ArrayList<PostModelResponse>>>?= _allPosts






    init {
        getPosts()
    }
    fun getPosts() {
        viewModelScope.launch {
            _allPosts!!.value = Resource.Loading
            val child1=  launch(Dispatchers.IO) {
                getAllPostsFromAPI {
                    launch (Dispatchers.Main){
                        _allPosts.value = it
                    }
                }
            }
        }
    }


}