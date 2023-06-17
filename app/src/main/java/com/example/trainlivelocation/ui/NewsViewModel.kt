package com.example.trainlivelocation.ui

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.GetNewsResponseItem
import com.example.domain.usecase.GetNews
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNews:GetNews
) :ViewModel(){
    private val _news:MutableLiveData<Resource<ArrayList<GetNewsResponseItem>>> = MutableLiveData(null)
     val news:LiveData<Resource<ArrayList<GetNewsResponseItem>>> = _news


    fun getNews(){
        viewModelScope.launch {
            _news.value=Resource.Loading
            val child1=launch (Dispatchers.IO){
                getNews(){
                    val child2 = launch (Dispatchers.Main){
                        _news.value=it
                    }
                }
            }
            child1.join()
        }
    }
}