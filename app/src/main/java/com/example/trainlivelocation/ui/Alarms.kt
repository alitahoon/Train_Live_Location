package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.databinding.FragmentAlarmsBinding


class Alarms : Fragment() {
    private val TAG: String? = "Alarms"
    private lateinit var binding: FragmentAlarmsBinding
    private val alarms_viewModel: Alarms_viewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAlarmsBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=alarms_viewModel
            }

        return binding.root
    }

    companion object {

    }
}