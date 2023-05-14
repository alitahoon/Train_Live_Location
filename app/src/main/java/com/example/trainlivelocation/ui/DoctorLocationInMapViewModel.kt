package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class DoctorLocationInMapViewModel @Inject constructor(

) : ViewModel() {
    private var MAP_VIEW_Bundle: Bundle?=null




    public fun getMAP_VIEW_KEY():Bundle?{
        MAP_VIEW_Bundle?.putString("MapViewBundleKey","101")
        return MAP_VIEW_Bundle
    }
}