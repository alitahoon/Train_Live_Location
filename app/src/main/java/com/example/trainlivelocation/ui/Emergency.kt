package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.databinding.FragmentEmergencyBinding

class Emergency : Fragment() {

    private val TAG: String? = "Emergency"
    private lateinit var binding: FragmentEmergencyBinding
    private val emergencyViewModel: EmergencyViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentEmergencyBinding.inflate(inflater,container,false)
            .apply {
                this.viewmode=emergencyViewModel
            }
        return  binding.root
    }

    companion object {

    }
}