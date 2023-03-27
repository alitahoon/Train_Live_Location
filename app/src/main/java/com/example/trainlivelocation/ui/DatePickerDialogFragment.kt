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
import com.example.trainlivelocation.utli.DatePickerListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class DatePickerDialogFragment(private val listener: DatePickerListener) : DialogFragment() {
    private var _binding: DatePickerDialogFragmentBinding? = null
    private val binding get() = _binding!!
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
        _binding = DatePickerDialogFragmentBinding.inflate(inflater, container, false)

        binding.datePickerDialogCalDateOfBirth.setOnDateChangeListener(object :OnDateChangeListener{
            override fun onSelectedDayChange(calendar: CalendarView, day: Int, month: Int, year: Int) {
                dateOfBirth="${day}-${month}-${year}"
            }
        })
        binding.datePickerBtnOk.setOnClickListener{
            listener.onDateSelected(dateOfBirth!!)
            dismiss()

        }
        binding.datePickerBtnCancel.setOnClickListener{
            listener.onDateSelected(dateOfBirth!!)
            dismiss()
        }
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}