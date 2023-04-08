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
import com.example.trainlivelocation.databinding.DatePickerDialogFragmentBinding
import com.example.trainlivelocation.databinding.DialogFragmentLocationLayoutBinding
import com.example.trainlivelocation.databinding.StationsDialogFragmentBinding
import com.example.trainlivelocation.utli.DatePickerListener
import com.example.trainlivelocation.utli.PostCustomAdapter
import com.example.trainlivelocation.utli.StationCustomAdapter
import com.example.trainlivelocation.utli.Station_Dialog_Listener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class ChooseStationDialogFragment(private val listener: Station_Dialog_Listener) : BottomSheetDialogFragment(),Station_Dialog_Listener {
    private var _binding: StationsDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private val chooseStationDialogFragmentViewModel : ChooseStationDialogFragmentViewModel by activityViewModels()
    private var stationId: Int?=null
    private val TAG:String?="ChooseStationDialogFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StationsDialogFragmentBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel=chooseStationDialogFragmentViewModel
            }
        chooseStationDialogFragmentViewModel!!.getAllStationFromApi()
        binding.adapter=setAdapterItems()
        setObservers()
        return binding.root
    }

    private fun setObservers() {

    }
    private fun setAdapterItems(): StationCustomAdapter {
        val adapter: StationCustomAdapter = StationCustomAdapter(this)
        chooseStationDialogFragmentViewModel.stationsData.observe(viewLifecycleOwner, Observer {
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

    override fun onStationSelected(StationId: Int?, StationName: String?,stationLocation:LocationDetails?) {
        listener.onStationSelected(StationId,StationName,stationLocation)
        dismiss()
    }


}