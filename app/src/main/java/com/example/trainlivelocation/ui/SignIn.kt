package com.example.trainlivelocation.ui

import Resource
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.UserSignInDataEntity
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentSignInBinding
import com.example.trainlivelocation.utli.LoadingDialogListener
import com.example.trainlivelocation.utli.SignInListener
import com.example.trainlivelocation.utli.showCustomToast
import com.example.trainlivelocation.utli.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
        resetDiffultDesign()

        setObservers()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        resetDiffultDesign()
    }

    private fun setObservers() {
        signInViewModel!!.signUpBtnClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(SignInDirections.actionSignInToAuthCheck())
            }
        })

        signInViewModel!!.signInBtnClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {

                signInViewModel!!.checkIfUserIsSignIn(
                )
                signInViewModel?.userLoginDataLive?.observe(viewLifecycleOwner,
                    Observer {
                        when (it) {
                            is Resource.Loading -> {
                                binding.signBtnSignIn.isEnabled = false
                                binding.signBtnSignIn.setBackgroundColor(requireContext().getColor(R.color.textAlarmColor))
                                binding.signInInputLayout.setVisibility(View.INVISIBLE)
                                binding.signInLoadingLayout.setVisibility(View.VISIBLE)
                                loading()
                            }

                            is Resource.Success -> {
                                Log.i(TAG, "Success:${it.data}")
                                signInViewModel!!.saveUserTokenInSharedPreferences(it.data)
                                signInViewModel!!.insertUserSignInData.observe(viewLifecycleOwner,
                                    Observer {
                                        when(it){
                                            is Resource.Loading->{
                                                Log.i(TAG,"cashing user data user data")
                                            }
                                            is Resource.Success->{
                                                Log.i(TAG,"${it.data}")
                                                findNavController().navigate(R.id.action_signIn_to_shareLocationDialog)
                                            }
                                            is Resource.Failure->{
                                                Log.i(TAG,"${it.error}")
                                            }
                                            else -> {}
                                        }
                                    })
                            }

                            is Resource.Failure -> {
                                resetDiffultDesign()
                                binding.signBtnSignIn.isEnabled = true
                                Log.e(TAG, it.error!!)
                            }

                            else -> {
                                resetDiffultDesign()
                                binding.signBtnSignIn.isEnabled = true
                                Log.e(TAG, "sign in when else brunch")
                            }
                        }
                    })
            }
        })


        signInViewModel!!.PhoneNumberIsNotCorrect.observe(viewLifecycleOwner, Observer {
            showCustomToast(requireContext(), "Please Enter Valid Phone number")
        })

    }

    fun loading() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
        }
    }

    fun resetDiffultDesign() {
        binding.signInLoadingLayout.setVisibility(View.GONE)
        binding.signInInputLayout.setVisibility(View.VISIBLE)
        binding.signBtnSignIn.isEnabled = true
        binding.signBtnSignIn.setBackgroundColor(requireContext().getColor(R.color.PrimaryColor))

    }

    companion object {

    }


}