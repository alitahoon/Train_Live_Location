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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllPostsViewModel @Inject constructor(
    private val getAllPostsFromAPI: GetAllPostsFromAPI
)  : ViewModel() {

    private val _allPosts:MutableLiveData<Resource<ArrayList<PostModelResponse>>>?= MutableLiveData(null)
    val allPosts:MutableLiveData<Resource<ArrayList<PostModelResponse>>>?= _allPosts

    init {
        getPosts()
    }
    fun getPosts(){
        _allPosts!!.value=Resource.Loading
        viewModelScope.launch {
            getAllPostsFromAPI{
                _allPosts.value=it
            }
        }
    }

}