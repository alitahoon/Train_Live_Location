package com.example.trainlivelocation.ui

import android.R
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trainlivelocation.databinding.DatePickerDialogFragmentBinding
import com.example.trainlivelocation.databinding.DialogFragmentLocationLayoutBinding
import com.example.trainlivelocation.utli.DatePickerListener
import com.example.trainlivelocation.utli.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class DatePickerDialogFragment(private val listener: DatePickerListener) : DialogFragment() {
    private var _binding: DatePickerDialogFragmentBinding? = null
    private val binding get() = _binding!!
    companion object{
         var dateOfBirth: String?="No Selected Date"
         var yearOfBirth: Int?=Calendar.getInstance().get(Calendar.YEAR)
    }
    private val TAG:String?="DatePickerDialogFragment"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DatePickerDialogFragmentBinding.inflate(inflater, container, false)

        binding.datePickerDialogCalDateOfBirth.setOnDateChangedListener(object : DatePicker.OnDateChangedListener{
            override fun onDateChanged(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                dateOfBirth="${p1}-${p2+1}-${p3}"
                yearOfBirth=p3
            }

        })
        binding.datePickerBtnOk.setOnClickListener{
            Log.i(TAG,"${Calendar.getInstance().get(Calendar.YEAR)},${yearOfBirth}")
            val age=Calendar.getInstance().get(Calendar.YEAR)-yearOfBirth!!
            if (age<=5){
                toast("Age must be bigger than 5 years old")
            }else{
                listener.onDateSelected(dateOfBirth!!)
                dismiss()
            }


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