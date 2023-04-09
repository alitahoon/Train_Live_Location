package com.example.trainlivelocation.ui

import Resource
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentSignInBinding
import com.example.trainlivelocation.utli.SignInListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignIn : Fragment() {
    private val signInViewModel: SignInViewModel? by viewModels()
    private lateinit var binding: FragmentSignInBinding
    private val TAG: String? = "sign_in_Fragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = signInViewModel
            }
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        signInViewModel!!.signUpBtnClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(SignInDirections.actionSignInToAuthCheck())
            }
        })

        signInViewModel!!.signInBtnClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {

                signInViewModel?.userLoginDataLive?.observe(viewLifecycleOwner,
                    Observer {
                       when(it){
                           is Resource.Loading->{

                           }
                           is Resource.Success->{

                           }
                           is Resource.Failure->{
                               Log.e(TAG,it.error!!)
                           }
                           else -> {
                               Log.e(TAG,"sign in when else brunch")
                           }
                       }
                    })
            }
        })
    }

    companion object {

    }

}