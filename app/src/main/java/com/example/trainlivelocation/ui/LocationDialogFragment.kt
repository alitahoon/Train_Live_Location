package com.example.trainlivelocation.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trainlivelocation.databinding.DialogFragmentLocationLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDialogFragment : BottomSheetDialogFragment() {
    private val locationDialogFragmentViewModel:LocationDialogFragmentViewModel? by activityViewModels()
    private var _binding: DialogFragmentLocationLayoutBinding? = null
    private val binding get() = _binding!!
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return AlertDialog.Builder(requireActivity())
//            .setView(_binding?.root)
//            .create()
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= DialogFragmentLocationLayoutBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=locationDialogFragmentViewModel
            }
        setObservers()
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun setObservers(){
        locationDialogFragmentViewModel?.trackLocationBtn?.observe(viewLifecycleOwner, Observer {
            if (it==true){
                val action=LocationDialogFragmentDirections.actionLocationDialogFragmentToTrackLocationFeature()
                findNavController().navigate(action)            }
        })
        locationDialogFragmentViewModel?.shareLocationBtn?.observe(viewLifecycleOwner, Observer {
            if (it==true){
                //btn ShareLocationBtn clicked
                val action=LocationDialogFragmentDirections.actionLocationDialogFragmentToShareLocationFeature()
                findNavController().navigate(action)

            }
        })
    }

}