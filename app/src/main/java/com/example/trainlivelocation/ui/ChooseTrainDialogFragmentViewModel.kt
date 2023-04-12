package com.example.trainlivelocation.ui

import Resource
import androidx.lifecycle.*
import com.example.domain.entity.TrainResponseItem
import com.example.domain.usecase.GetAllTrains
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChooseTrainDialogFragmentViewModel @Inject constructor(
    private val getAllTrains: GetAllTrains
):ViewModel(){
    val  staionName= MutableLiveData<String>()

    private val _TrainData:MutableLiveData<Resource<ArrayList<TrainResponseItem>?>> = MutableLiveData(null)
     val TrainData:LiveData<Resource<ArrayList<TrainResponseItem>?>> = _TrainData




    fun getAllTrainsFromApi(){
        _TrainData.value=Resource.Loading
        viewModelScope.launch{
            getAllTrains{
                _TrainData.value=it
            }
        }
    }




    inline fun <T> LiveData<T>.filter(crossinline filter: (T?) -> Boolean): LiveData<T> {
        return MediatorLiveData<T>().apply {
            addSource(this@filter) {
                if (filter(it)) {
                    this.value = it
                }
            }
        }
    }

}