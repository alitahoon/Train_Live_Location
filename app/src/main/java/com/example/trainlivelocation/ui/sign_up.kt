package com.example.trainlivelocation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentSignUpBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Sign_up.newInstance] factory method to
 * create an instance of this fragment.
 */
class Sign_up : Fragment(),SignUpListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val registerViewModel:UserSignUpViewModel? by activityViewModels()
    private lateinit var binding: FragmentSignUpBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSignUpBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=registerViewModel
            }
        registerViewModel?.setbaseActivity(requireActivity())
        binding.viewmodel?.userSignUpListener=this

        var jobArrayAdapter:ArrayAdapter<CharSequence> =ArrayAdapter.createFromResource(requireContext(),R.array.jopsArray,R.layout.spinner_jop_item_layout)
        jobArrayAdapter.setDropDownViewResource(R.layout.spinner_job_dropdown_item_layout)
        binding.RegisterSpinnerJobs.adapter=jobArrayAdapter


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment sign_up.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Sign_up().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStartSignUp() {
    }

    override fun onSuccessSignUp() {
        //start dialog otb loading fragment
        //get user post object
//        registerViewModel?.userDataLive?.observe(viewLifecycleOwner,
//        Observer {
//            if (it != null){
//                binding.registerProgressBar.setVisibility(View.GONE)
//                Toast.makeText(requireContext(), "done", Toast.LENGTH_SHORT).show()
//                Log.e("register",it.toString())
//            }
//        })
        }

    override fun onOtbCodeSendToUser() {
    }

    override fun onVerificationCompleted() {
    }

    override fun onVerficationSuccess() {
        TODO("Not yet implemented")
    }

    override fun nextBtnClicked(type:String) {
        when(type){
            "register" -> {
                findNavController().navigate(R.id.action_sign_up_to_authCheck)
            }
        }
        binding.signUpFirstPhaseLayout.setVisibility(View.GONE)
        binding.signUpSecondPhaseLayout.setVisibility(View.VISIBLE)
    }

    override fun onFailure(message: String) {
        Log.e("registerOnFailure",message)
    }


}