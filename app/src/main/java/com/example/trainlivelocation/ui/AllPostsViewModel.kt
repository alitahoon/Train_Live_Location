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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllPostsViewModel @Inject constructor(
    private val getAllPostsFromAPI: GetAllPostsFromAPI,
    private val context: Context
)  : ViewModel() {
    private val sharedPrefFile = "UserToken"


    private val _allPosts:MutableLiveData<Resource<ArrayList<PostModelResponse>>>?= MutableLiveData(null)
    val allPosts:MutableLiveData<Resource<ArrayList<PostModelResponse>>>?= _allPosts


    private val _userData: MutableLiveData<UserResponseItem?> = MutableLiveData(null)
    val userData: LiveData<UserResponseItem?> = _userData



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

    fun getUserDataFromsharedPreference() {
        val userSharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        _userData.postValue(
            UserResponseItem(
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