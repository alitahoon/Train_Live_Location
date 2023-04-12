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
import com.example.trainlivelocation.databinding.TrainDialogFragmentBinding
import com.example.trainlivelocation.utli.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChooseTrainDialogFragment(private val listener: Train_Dialog_Listener) : BottomSheetDialogFragment(),Train_Dialog_Listener {
    private var _binding: TrainDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private val chooseTrainDialogFragmentViewModel : ChooseTrainDialogFragmentViewModel by activityViewModels()
    private var stationId: Int?=null
    private val TAG:String?="ChooseStationDialogFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TrainDialogFragmentBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel=chooseTrainDialogFragmentViewModel
            }
        chooseTrainDialogFragmentViewModel!!.getAllTrainsFromApi()
        _binding!!.adapter=setAdapterItems()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        chooseTrainDialogFragmentViewModel.staionName.observe(viewLifecycleOwner, Observer {
            Log.i(TAG,"${it}")
//            val typedName=it
//            val filteredData=binding.adapter!!.stationArrayList.filter {
//                it.name==typedName
//            }
//            binding.adapter.setData(ArrayList(filteredData))
        })
    }
    private fun setAdapterItems(): TrainCustomAdapter {
        val adapter: TrainCustomAdapter = TrainCustomAdapter(this)
        chooseTrainDialogFragmentViewModel.TrainData.observe(viewLifecycleOwner, Observer {
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
                    Log.i(TAG,"Success+${it.data}")
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

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        listener.onTrainSelected(trainId,trainDegree)
        dismiss()
    }


}