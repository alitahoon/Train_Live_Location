package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentReportPostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Report_Post : Fragment() {
    private var binding:FragmentReportPostBinding=null
    private val report_Post_ViewModel: Report_Post_ViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportPostBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=report_Post_ViewModel
            }
    }

    companion object {

    }
}