package com.example.trainlivelocation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.DoctorResponseItem
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.databinding.FragmentEmergencyBinding
import com.example.trainlivelocation.utli.DoctorCustomAdapter
import com.example.trainlivelocation.utli.DoctorListener
import com.example.trainlivelocation.utli.MessageCustomAdapter
import com.example.trainlivelocation.utli.Train_Dialog_Listener

class Emergency : Fragment() ,DoctorListener,Train_Dialog_Listener{

    private val TAG: String? = "Emergency"
    private lateinit var binding: FragmentEmergencyBinding
    private val emergencyViewModel: EmergencyViewModel by activityViewModels()
    var adapter :DoctorCustomAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmergencyBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = emergencyViewModel
            }
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        emergencyViewModel.txtChooseTrainIdClicked.observe(viewLifecycleOwner, Observer {
            if (it ==true){
                //get train inforamtion
                var dialog = ChooseTrainDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseTrainDialogFragment")
            }
        })
    }

    private fun setAdapterItems(): DoctorCustomAdapter {
        val adapter = DoctorCustomAdapter(this)
        emergencyViewModel.doctors.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.doctorFragmentLoading.visibility=View.VISIBLE
                    binding.doctorRcv.visibility=View.GONE
                    Log.i(TAG, "messages is loading ....")
                }
                is Resource.Success -> {
                    binding.doctorFragmentLoading.visibility=View.GONE
                    binding.doctorRcv.visibility=View.VISIBLE
                    Log.i(TAG, "data : ${it.data}")
                    adapter.setData(it.data)

                }
                is Resource.Failure -> {
                    binding.doctorFragmentLoading.visibility=View.GONE
                    binding.doctorRcv.visibility=View.GONE
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

    override fun OnNotifyClickListener(doctor: DoctorResponseItem) {
        TODO("Not yet implemented")
    }

    override fun OnChatClickListener(doctor: DoctorResponseItem) {
        TODO("Not yet implemented")
    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        binding.trackLocationTxtTrainId.setText(trainId.toString())
        emergencyViewModel.getDoctors(trainId)
        binding.doctorRcv.adapter=setAdapterItems()
    }
}