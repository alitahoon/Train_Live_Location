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
import com.example.domain.entity.UserInTrainResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentEmergencyBinding
import com.example.trainlivelocation.databinding.FragmentPassengersBinding
import com.example.trainlivelocation.utli.*


class Passengers : Fragment(),PassengersListener, Train_Dialog_Listener {

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
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        passengersViewModel.txtChooseTrainIdClicked.observe(viewLifecycleOwner, Observer {
            if (it==true){
                //get train inforamtion
                var dialog = ChooseTrainDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseTrainDialogFragment")
            }
        })
    }

    private fun setAdapterItems(): PassengersCustomAdapter {
        val adapter = PassengersCustomAdapter(this)
        passengersViewModel.passengers.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.passengersShimmerLoading.visibility = View.VISIBLE
                    binding.passengersRcv.visibility = View.GONE
                    Log.i(TAG, "messages is loading ....")
                }
                is Resource.Success -> {
                    binding.passengersShimmerLoading.visibility = View.GONE
                    binding.passengersRcv.visibility = View.VISIBLE
                    Log.i(TAG, "data : ${it.data}")
                    adapter.setData(it.data)

                }
                is Resource.Failure -> {
                    binding.passengersShimmerLoading.visibility = View.GONE
                    binding.passengersRcv.visibility = View.GONE
                    Log.e(TAG, "${it.error}")
                }
                else -> {

                }
            }
        })
        return adapter
    }

    companion object {

    }

    override fun OnChatClickListener(passengers: UserInTrainResponseItem) {
        if (passengers.userPhone != args.userModel.phone) {
            var dialog = Chat(passengers.userPhone, passengers.userName, args.userModel)
            var childFragmentManager = getChildFragmentManager()
            dialog.show(childFragmentManager, "Chat")
        } else {
            toast("You Can't chat With your self...")
        }
    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        binding.passengersTxtTrainId.setText("${trainId}")
        passengersViewModel.getPassengers(trainId)
        binding.passengersRcv.adapter = setAdapterItems()
    }
}