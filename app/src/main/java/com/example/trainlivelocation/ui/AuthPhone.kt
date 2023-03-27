package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAuthPhoneBinding
import com.example.trainlivelocation.databinding.FragmentHomeBinding
import com.example.trainlivelocation.utli.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthPhone : Fragment() {
    private val authPhoneViewmodel:AuthPhoneViewmodel? by activityViewModels()
    private var binding: FragmentAuthPhoneBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAuthPhoneBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=authPhoneViewmodel
            }
        setObservers()
        authPhoneViewmodel?.setActivity(requireActivity() as AppCompatActivity)
        authPhoneViewmodel?.setFirbaseActivity()
        return binding!!.root
    }

    private fun setObservers() {
        authPhoneViewmodel?.sendCodebtn!!.observe(viewLifecycleOwner, Observer {
            if (it == true){
//                binding!!.authPhoneFirstLayout.setVisibility(View.GONE)
//                binding!!.authPhonesSecondLayout.setVisibility(View.GONE)
//                binding!!.CodeVerficationDialogLoadingCodeSending.setVisibility(View.VISIBLE)
//                authPhoneViewmodel?.sendOtb()
                findNavController().navigate(AuthPhoneDirections.actionAuthPhoneToSignUp(binding!!.phone.text.toString()))
            }
        })
        authPhoneViewmodel?.onCodeSent!!.observe(viewLifecycleOwner, Observer {
            if (it == true){
                val action=AuthPhoneDirections.actionAuthPhoneToAuthCode(binding!!.phone.text.toString())
                findNavController().navigate(action)
            }
        })
        authPhoneViewmodel?.onAuthFailed!!.observe(viewLifecycleOwner, Observer {
            if (it == true){
                binding!!.authPhoneFirstLayout.setVisibility(View.VISIBLE)
                binding!!.authPhonesSecondLayout.setVisibility(View.VISIBLE)
                binding!!.CodeVerficationDialogLoadingCodeSending.setVisibility(View.GONE)
                toast("Verification failed please try again..")
            }
        })
    }

    companion object {

    }
}