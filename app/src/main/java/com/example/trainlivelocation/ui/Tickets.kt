package com.example.trainlivelocation.ui

import Resource
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.domain.entity.StationResponseItem
import com.example.domain.entity.TicketRequestItem
import com.example.domain.entity.Train
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentInboxRecieveBinding
import com.example.trainlivelocation.databinding.FragmentTicketsBinding
import com.example.trainlivelocation.utli.*

class Tickets : Fragment(), Station_Dialog_Listener, Train_Dialog_Listener,Arrival_Station_Listener {
    private val args by navArgs<TicketsArgs>()

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
        setObserver()
        return binding.root
    }

    private fun setObserver() {

        ticketsViewModel.btnAddTicketClicked.observe(viewLifecycleOwner, Observer {
            if (it==true){
                Log.i(TAG,"btnAddTicketClicked")
                val model=TicketRequestItem(
                    arrivalStation!!,0.0,"2023-04-25T21:49:14.4",takeoffStation!!,trainDegree!!,trainId!!,trainNumber!!, args.userModel!!.id
                )

                ticketsViewModel.addTicketToApi(model)
                Log.i(TAG,"${model}")

                ticketsViewModel.ticket.observe(viewLifecycleOwner, Observer {
                    when(it){
                        is Resource.Loading->{
                            toast("Loading To book ticket")
                        }
                        is Resource.Success->{
                            toast("Success To book ticket--->{${it.data}}")
                        }
                        is Resource.Failure->{
                            Log.e(TAG,"Error from else brunsh---->ticket ${it.error}")
                            toast("Failed To book ticket")
                        }

                        else -> {
                            Log.e(TAG,"Error from else brunsh---->ticket")
                        }
                    }
                })
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
    }

    override fun onStationSelected(
        StationId: Int?,
        StationName: String?,
        longitude: Double?,
        latitude: Double?
    ) {
        binding.TicketTxtTakeoff.setText(StationName)
        takeoffStation=StationName

    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        binding.ticketTxtTrainId.setText(trainId.toString())
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
        binding.ticketTxtArrival.setText(StationName)
        arrivalStation=StationName
    }
}