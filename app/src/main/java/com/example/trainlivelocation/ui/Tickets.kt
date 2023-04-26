package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentInboxRecieveBinding
import com.example.trainlivelocation.databinding.FragmentTicketsBinding
import com.example.trainlivelocation.utli.Station_Dialog_Listener
import com.example.trainlivelocation.utli.Train_Dialog_Listener

class Tickets : Fragment() ,Station_Dialog_Listener,Train_Dialog_Listener{

    private val TAG: String? = "Tickets"
    private lateinit var binding: FragmentTicketsBinding
    private val ticketsViewModel: TicketsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTicketsBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodle=ticketsViewModel
            }
        setObserver()

        return binding.root
    }

    private fun setObserver() {
        ticketsViewModel.arrivalStationTxtClicked.observe(viewLifecycleOwner, Observer {
            if (it==true){
                //get arrival inforamtion
                var dialog = ChooseStationDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseStationDialogFragment")
            }
        })


        ticketsViewModel.takroffStationTxtClicked.observe(viewLifecycleOwner, Observer {
            if (it==true){
                //get arrival inforamtion
            }
        })

        ticketsViewModel.trainIdTxtClicked.observe(viewLifecycleOwner, Observer {
            if (it==true){
                //get train inforamtion
                var dialog = ChooseStationDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseStationDialogFragment")
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

    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {

    }
}