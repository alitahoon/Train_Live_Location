package com.example.trainlivelocation.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
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
import com.example.trainlivelocation.databinding.FragmentLogoLoadingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Logo_loading : Fragment() {
    private var binding:FragmentLogoLoadingBinding?=null
    private val logo_loading_Viewmodel: Logo_loading_Viewmodel? by viewModels()
    private val TAG="Logo_loading"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLogoLoadingBinding.inflate(inflater,container,false)
            .apply {

            }


        // Scale from 0% to 100%
        val scaleXAnimator = ObjectAnimator.ofFloat(binding!!.logoLoadingLogo, "scaleX", 0f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(binding!!.logoLoadingTxtTitle, "scaleY", 0f, 1f)

        // Set duration in milliseconds (3 seconds)
        scaleXAnimator.duration = 1000
        scaleYAnimator.duration = 1000

        // Add an AnimatorListener to perform an action when the animation finishes
        scaleXAnimator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                //check if the app is running for the first time


                logo_loading_Viewmodel!!.checkingUserData()
                logo_loading_Viewmodel!!.getUserSignInData.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is Resource.Loading -> {
                            Log.i(TAG, "getting user date")
                        }
                        is Resource.Success -> {
                            Log.i(TAG, "${it.data}")
                            if (it.data.isNotEmpty()){
                                for (user in it.data) {
                                    logo_loading_Viewmodel!!.checkIfUserIsSignIn(
                                        userPhone = user.userName,
                                        userPassword = user.password
                                    )
                                }
                                logo_loading_Viewmodel!!.userLoginDataLive.observe(viewLifecycleOwner, Observer {
                                    when (it) {
                                        is Resource.Failure -> {
                                            Log.e(TAG, "${it.error}")
                                        }
                                        is Resource.Success -> {
                                            findNavController().navigate(R.id.action_logo_loading_to_chooseTrainDialogFragment)
                                        }
                                        is Resource.Loading -> {
                                            Log.i(TAG, "checking user info")

                                        }
                                        else -> {}
                                    }
                                })
                            }else{
                                findNavController().navigate(R.id.action_logo_loading_to_splash2)
                            }

                        }
                        is Resource.Failure -> {
                            Log.e(TAG, "${it.error}")
                        }
                        else -> {}
                    }
                })

            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

        // Start the animation
        scaleXAnimator.start()
        scaleYAnimator.start()

        return binding!!.root
    }

    companion object {

    }
}