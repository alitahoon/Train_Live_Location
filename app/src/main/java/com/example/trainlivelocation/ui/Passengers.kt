package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentEmergencyBinding
import com.example.trainlivelocation.databinding.FragmentPassengersBinding
import com.example.trainlivelocation.utli.DoctorCustomAdapter


class Passengers : Fragment() {

    private val TAG: String? = "Passengers"
    private lateinit var binding: FragmentPassengersBinding
    private val passengersViewModel: PassengersViewModel by activityViewModels()
    private val args by navArgs<PassengersArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPassengersBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=passengersViewModel
            }
        return binding.root
    }

    companion object {

    }
}