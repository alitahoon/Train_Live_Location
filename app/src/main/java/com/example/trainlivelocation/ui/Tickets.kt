package com.example.trainlivelocation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.StationResponseItem
import com.example.domain.entity.TicketRequestItem
import com.example.domain.entity.Train
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentInboxRecieveBinding
import com.example.trainlivelocation.databinding.FragmentTicketsBinding
import com.example.trainlivelocation.utli.Arrival_Station_Listener
import com.example.trainlivelocation.utli.Station_Dialog_Listener
import com.example.trainlivelocation.utli.Train_Dialog_Listener

class Tickets : Fragment(), Station_Dialog_Listener, Train_Dialog_Listener,Arrival_Station_Listener {

    private val TAG: String? = "Tickets"
    private lateinit var binding: FragmentTicketsBinding
    private val ticketsViewModel: TicketsViewModel by activityViewModels()
    private var takeoffStation: String?=null
    private var arrivalStation: String?=null
    private var trainId: Int?=null
    private var trainDegree: String?=null
    private var trainNumber: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketsBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = ticketsViewModel
            }
        ticketsViewModel.getUserData()
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        ticketsViewModel.addTicketToApi(TicketRequestItem(
            arrivalStation!!,0,takeoffStation!!,trainDegree!!,trainId!!,trainNumber!!, user!!.id
        ))

        ticketsViewModel.userData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.i(TAG, "loading User data....")
                }
                is Resource.Success -> {
                    user = it.data
                }
                is Resource.Failure -> {
                    Log.i(TAG, "${it.error}")
                }

                else -> {}
            }
        })

        ticketsViewModel.takroffStationTxtClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                //get arrival inforamtion
                var dialog = ChooseStationDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseStationDialogFragment")
            }
        })


        ticketsViewModel.arrivalStationTxtClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                //get arrival inforamtion
                var dialog = ChooseArrivalStationFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseArrivalStationFragment")
            }
        })

        ticketsViewModel.trainIdTxtClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                //get train inforamtion
                var dialog = ChooseTrainDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseTrainDialogFragment")
            }
        })

    }

    companion object {
        private var user:UserResponseItem?=null
    }

    override fun onStationSelected(
        StationId: Int?,
        StationName: String?,
        longitude: Double?,
        latitude: Double?
    ) {
        takeoffStation=StationName
    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        this.trainId=trainId
        this.trainDegree=trainDegree
        this.trainNumber=trainId.toString()
    }

    override fun onArrivalStationSelected(
        StationId: Int?,
        StationName: String?,
        longitude: Double?,
        latitude: Double?
    ) {
        arrivalStation=StationName
    }
}