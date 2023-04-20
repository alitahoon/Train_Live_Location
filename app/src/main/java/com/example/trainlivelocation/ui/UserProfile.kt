package com.example.trainlivelocation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentUserProfileBinding
import com.example.trainlivelocation.utli.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfile : Fragment() {
    private val TAG:String?="UserProfile"
    private lateinit var binding: FragmentUserProfileBinding
    private val userProfileViewModel : UserProfileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userModel=UserProfileArgs.fromBundle(requireArguments()).userModel
        binding= FragmentUserProfileBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=userProfileViewModel
                this.userModel=userModel
            }
        userProfileViewModel.getStationName(userModel.stationId)
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        userProfileViewModel.stationName.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    binding.profileTxtStation.setText("Loading...")
                    Log.i(TAG,"${it}")
                }
                is Resource.Success->{
                    binding.profileTxtStation.setText(it.data!!.name)
                    Log.i(TAG,"${it}")
                }
                is Resource.Failure->{
                    Log.e(TAG,"Error while loading station name from api ---> ${it.error}")
                    toast("Error getting Station Name")
                }
                else -> {
                    Log.e(TAG,"Error while loading station name from api ---> else brunsh")
                }
            }
        })
    }

    companion object {

    }
}