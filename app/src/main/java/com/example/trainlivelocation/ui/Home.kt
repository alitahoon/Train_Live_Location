package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.userResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.DialogFragmentLocationLayoutBinding
import com.example.trainlivelocation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : Fragment() {


    private val homeViewModel:HomeViewModel? by activityViewModels()
    private var binding: FragmentHomeBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=homeViewModel
            }
        setObservers()

        homeViewModel?.getUserDataFromsharedPreference()

        if (getDistance()!=null){
            binding?.homeTxtTrainDistance?.setText(getDistance().toString()+" Meal")
        }

        return binding!!.root
    }

    private fun setObservers() {
        homeViewModel?.locationBtn?.observe(viewLifecycleOwner, Observer {
            if (it==true){
                val action= HomeDirections.actionHomeToLocationDialogFragment()
                findNavController().navigate(action)
            }
        })


        homeViewModel?.postsBtn?.observe(viewLifecycleOwner, Observer {
            if (it==true){
                findNavController().navigate(HomeDirections.actionHomeToPosts())
            }
        })


        homeViewModel?.locationCardBtn?.observe(viewLifecycleOwner, Observer {
            if (it==true){
                findNavController().navigate(R.id.action_home2_to_trainLocationInMap)
            }
        })

        homeViewModel?.userData!!.observe(viewLifecycleOwner, Observer {
            userModel=it
        })
    }
    fun getDistance():Float?{
        return arguments?.getFloat("distance")!!
    }

    companion object {
        var userModel:userResponseItem?=null
    }
}