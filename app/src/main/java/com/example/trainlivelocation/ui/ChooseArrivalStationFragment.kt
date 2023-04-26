package com.example.trainlivelocation.ui

import android.R
import android.os.Build
import Resource
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.LocationDetails
import com.example.trainlivelocation.databinding.ArrivalStationFragmentBinding
import com.example.trainlivelocation.databinding.DatePickerDialogFragmentBinding
import com.example.trainlivelocation.databinding.DialogFragmentLocationLayoutBinding
import com.example.trainlivelocation.databinding.StationsDialogFragmentBinding
import com.example.trainlivelocation.utli.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChooseArrivalStationFragment(private val listener: Arrival_Station_Listener) : BottomSheetDialogFragment(),Station_Dialog_Listener {
    private var _binding: ArrivalStationFragmentBinding? = null
    private val binding get() = _binding!!
    private val chooseArrivalStationFragmentViewModel : ChooseArrivalStationFragmentViewModel by activityViewModels()
    private var stationId: Int?=null
    private val TAG:String?="ChooseArrivalStationFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ArrivalStationFragmentBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel=chooseArrivalStationFragmentViewModel
            }
        chooseArrivalStationFragmentViewModel!!.getAllStationFromApi()
        binding.adapter=setAdapterItems()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        chooseArrivalStationFragmentViewModel.staionName.observe(viewLifecycleOwner, Observer {
            Log.i(TAG,"${it}")
//            val typedName=it
//            val filteredData=binding.adapter!!.stationArrayList.filter {
//                it.name==typedName
//            }
//            binding.adapter.setData(ArrayList(filteredData))
        })
    }
    private fun setAdapterItems(): StationCustomAdapter {
        val adapter: StationCustomAdapter = StationCustomAdapter(this)
        chooseArrivalStationFragmentViewModel.stationsData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    binding.stationDialogRcv.visibility=View.INVISIBLE
//                    binding.stationDialogRcv.setVisibility(View.INVISIBLE)
//                    binding.stationDialogProgressBar.setVisibility(View.VISIBLE)
                    Log.i(TAG,"Loading")

                }
                is Resource.Success->{
                    binding.stationDialogShimmerLoading.stopShimmer()
                    binding.stationDialogShimmerLoading.visibility=View.INVISIBLE
                    binding.stationDialogRcv.visibility=View.VISIBLE
                    Log.i(TAG,"Success")
                    adapter.setData(it.data!!)
//                    binding.stationDialogRcv.setVisibility(View.VISIBLE)
//                    binding.stationDialogProgressBar.setVisibility(View.GONE)
                }

                else -> {

                }
            }

        })
        return adapter
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onArrivalStationSelected(
        StationId: Int?,
        StationName: String?,
        longitude: Double?,
        latitude: Double?
    ) {
        listener.onArrivalStationSelected(StationId,StationName,longitude,latitude)
        dismiss()
    }


}