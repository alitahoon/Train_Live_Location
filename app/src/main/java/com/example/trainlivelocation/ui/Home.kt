package com.example.trainlivelocation.ui

import Resource
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.NotificatonToken
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentHomeBinding
import com.example.trainlivelocation.utli.PermissionManager
import com.example.trainlivelocation.utli.TrackTrainService
import com.example.trainlivelocation.utli.Train_Dialog_Listener
import com.example.trainlivelocation.utli.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : Fragment() ,Train_Dialog_Listener{
    private val TAG:String?="Home";

    private val homeViewModel:HomeViewModel? by activityViewModels()
    private var binding: FragmentHomeBinding? = null
    private val permissionManager = PermissionManager.from(this)


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
        homeViewModel?.subscribeToNewTopic()
        if (getDistance()!=null){
            binding?.homeTxtTrainDistance?.setText(getDistance().toString()+" Meal")
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionManager
                .request(Permission.Notification)
                .rationale("We need permission to show Notifications")
                .checkPermission { granted: Boolean ->
                    if (granted) {
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No Permission to show notifications",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }


        return binding!!.root
    }

    private fun setObservers() {
        homeViewModel!!.sendingNotificationToken.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.e(TAG, "sending token...")
                }
                is Resource.Success -> {
                    Log.e(TAG, "${it.data}")
                }
                is Resource.Failure -> {
                    Log.e(TAG, "${it.error}")
                }
                else -> {
                    Log.e(TAG, "sendingNotificationToken else brunch...")

                }
            }
        })

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

        homeViewModel?.passengersbtnClicked?.observe(viewLifecycleOwner, Observer {
            if (it==true){
                val action=HomeDirections.actionHome2ToPassengers(userModel!!)
                findNavController().navigate(action)
            }
        })

        homeViewModel?.userData!!.observe(viewLifecycleOwner, Observer {
            userModel=it
            if (userModel!=null){
                homeViewModel!!.sendingTokenToFirebase(NotificatonToken(userModel!!.phone,userModel!!.name," "))
            }
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


                }

                else -> {}
            }
        })
    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        binding!!.homeTrackTrainIDTxt.setText(trainId!!.toString())
        homeViewModel!!.getTrainLocationInbackground(trainId)
        var locationForegrondservice: Intent?= Intent(requireActivity(),TrackTrainService::class.java)
        locationForegrondservice!!.putExtra("trainId",binding!!.homeTrackTrainIDTxt.text.toString().toInt())
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            requireActivity().startForegroundService(locationForegrondservice)
        }else{
            requireActivity().startService(locationForegrondservice)
        }
    }
}