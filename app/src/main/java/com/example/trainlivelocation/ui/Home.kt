package com.example.trainlivelocation.ui

import Resource
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.GetTrainLocationInForgroundService
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentHomeBinding
import com.example.trainlivelocation.utli.Train_Dialog_Listener
import com.example.trainlivelocation.utli.toast
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
                val action=HomeDirections.actionHome2ToEmergency(userModel!!)
                findNavController().navigate(action)
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

    fun observeTrainLocationService(){
        homeViewModel!!.trainbackgroundTrackingServices.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    toast("getting service please wait...")
                }
                is Resource.Success->{
                    toast("getting service successfully...")
                    var locationForegrondservice: Intent?= Intent(requireActivity(),it::class.java)
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                        toast("done")
                        requireActivity().startForegroundService(locationForegrondservice)
                    }
                    toast("done")
                    requireActivity().startService(locationForegrondservice)
                }

                else -> {}
            }
        })
    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        binding!!.homeTrackTrainTxt.setText(trainId!!.toString())
        homeViewModel!!.getTrainLocationInbackground(trainId)
        observeTrainLocationService()
    }
}