package com.example.trainlivelocation.ui

import android.R
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trainlivelocation.databinding.DatePickerDialogFragmentBinding
import com.example.trainlivelocation.databinding.DialogFragmentLocationLayoutBinding
import com.example.trainlivelocation.databinding.StationsDialogFragmentBinding
import com.example.trainlivelocation.utli.DatePickerListener
import com.example.trainlivelocation.utli.Station_Dialog_Listener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class ChooseStationDialogFragment(private val listener: Station_Dialog_Listener) : DialogFragment() {
    private var _binding: StationsDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private val chooseStationDialogFragmentViewModel : ChooseStationDialogFragmentViewModel by activityViewModels()
    private var dateOfBirth: String?="No Selected Date"

    //    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return AlertDialog.Builder(requireActivity())
//            .setView(_binding?.root)
//            .create()
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StationsDialogFragmentBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel=chooseStationDialogFragmentViewModel
            }
        setObservers()
        return binding.root
    }

    private fun setObservers() {

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}