package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentHomeBinding
import com.example.trainlivelocation.utli.Train_Dialog_Listener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : Fragment() ,Train_Dialog_Listener{


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
                val action= HomeDirections.actionHomeToLocationDialogFragment(userModel!!)
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

        homeViewModel?.btnEmergancyClicked?.observe(viewLifecycleOwner, Observer {
            if (it==true){
                findNavController().navigate(R.id.action_home2_to_emergency)
            }
        })

        homeViewModel?.btnTicketClicked?.observe(viewLifecycleOwner, Observer {
            if (it==true){
                val action=HomeDirections.actionHome2ToTickets(userModel!!)
                findNavController().navigate(action)
            }
        })

        homeViewModel?.userData!!.observe(viewLifecycleOwner, Observer {
            userModel=it
        })

        homeViewModel?.chooseTrainTxtClicked!!.observe(viewLifecycleOwner, Observer {
            if (it==true){
                var dialog = ChooseTrainDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseTrainDialogFragment")
            }
        })
    }
    fun getDistance():Float?{
        return arguments?.getFloat("distance")!!
    }

    companion object {
        var userModel:UserResponseItem?=null
    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        binding!!.homeTrackTrainTxt.setText(trainId!!.toString())
    }
}