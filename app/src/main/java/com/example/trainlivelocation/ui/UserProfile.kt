package com.example.trainlivelocation.ui

import Resource
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.request.transition.ViewPropertyTransition.Animator
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentUserProfileBinding
import com.example.trainlivelocation.utli.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfile : Fragment() {
    private val TAG: String? = "UserProfile"
    private lateinit var binding: FragmentUserProfileBinding
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var user: UserResponseItem?=UserProfileArgs.fromBundle(requireArguments()).userModel
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = userProfileViewModel
                this.userModel = user
            }

        binding.root.postDelayed({
            var jobArrayAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.jopsArray,
                R.layout.spinner_jop_item_layout
            )
            jobArrayAdapter.setDropDownViewResource(R.layout.spinner_job_dropdown_item_layout)
            binding.profileSpinnerJobs.adapter=jobArrayAdapter

            setSpinnerSelectionByValue(user!!.jop)
            setObserver()
        },2000)


        return binding.root
    }

    private fun setObserver() {

        userProfileViewModel.btnSaveUserDateClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                var user: UserResponseItem?=UserProfileArgs.fromBundle(requireArguments()).userModel
                val userUpdatedData =    RegisterUser(
                    user!!.address,
                    user!!.birthDate,
                    binding.profileTxtEmail.text.toString(),
                    user!!.gender,
                    binding.profileSpinnerJobs.selectedItem.toString(),
                    binding.profileTxtUsername.text.toString(),
                    binding.profileTxtPassword.text.toString(),
                    binding.profileTxtPhone.text.toString(),
                    user!!.role,
                    user.tokenForNotifications,
                    null
                )
                userProfileViewModel.updateUserProfileData(user!!.id,
                    userRequest = userUpdatedData
                )

                Log.i(TAG,"${userUpdatedData}")

                userProfileViewModel.userUpdated.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is Resource.Loading -> {
                            binding.profileMainLayout.setBackgroundColor(requireContext().getColor(R.color.white))
                            binding.profileTopShape.visibility = View.GONE
                            binding.profileDataLayout.visibility = View.GONE
                            binding.profileLoadingLotti.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {

                            //clearing user cashing data
                            userProfileViewModel.clearUserData()
                            userProfileViewModel.clearUserData.observe(viewLifecycleOwner, Observer {
                                when(it){
                                    is Resource.Loading->{
                                        Log.i(TAG,"clearing user data")
                                    }
                                    is Resource.Failure->{
                                        Log.e(TAG,"${it.error}")
                                    }
                                    is Resource.Success->{
                                        Log.i(TAG,"${userUpdatedData}")
                                        Log.i(TAG, "success ${it.data}")
                                        userProfileViewModel.saveUserTokenInSharedPreferences(UserResponseItem(
                                            address = userUpdatedData.address,
                                            birthDate = userUpdatedData.birthDate,
                                            email = userUpdatedData.email,
                                            gender = userUpdatedData.gender,
                                            id=user.id,
                                            jop = userUpdatedData.jop,
                                            name = userUpdatedData.name,
                                            password = userUpdatedData.password,
                                            phone = userUpdatedData.phone,
                                            role = userUpdatedData.role,
                                            tokenForNotifications = user.tokenForNotifications
                                        ))
                                        binding.profileMainLayout.setBackgroundColor(requireContext().getColor(R.color.white))
                                        binding.profileTopShape.visibility = View.GONE
                                        binding.profileDataLayout.visibility = View.GONE
                                        binding.profileLoadingLotti.visibility = View.GONE
                                        binding.profileUpdateUserSeuccess.visibility = View.VISIBLE
                                        binding.profileUpdateUserSeuccess.addAnimatorListener(object :
                                            android.animation.Animator.AnimatorListener {
                                            override fun onAnimationStart(p0: android.animation.Animator) {
                                                TODO("Not yet implemented")
                                            }

                                            override fun onAnimationEnd(p0: android.animation.Animator) {
                                                findNavController().navigate(R.id.action_userProfile_to_splash3)
                                            }

                                            override fun onAnimationCancel(p0: android.animation.Animator) {
                                                TODO("Not yet implemented")
                                            }

                                            override fun onAnimationRepeat(p0: android.animation.Animator) {
                                                TODO("Not yet implemented")
                                            }

                                        })

                                    }
                                    else -> {}
                                }
                            })
                        }
                        is Resource.Failure -> {
                            Log.e(TAG, "${it.error}")
                        }
                        else -> {
                            resetLayout()
                            Log.e(TAG, "Failed To Update user Data else brunch")
                        }
                    }
                })
            }
        })

    }

    fun setSpinnerSelectionByValue(value: String) {
        val spinner: Spinner = binding.profileSpinnerJobs// Replace R.id.spinner with your spinner's ID

        val desiredName = value

        val spinnerData = resources.getStringArray(R.array.jopsArray) // Replace R.array.spinner_data with your data source

        var selectedIndex = -1
        for (i in spinnerData.indices) {
            if (spinnerData[i] == desiredName) {
                selectedIndex = i
                break
            }
        }

// Set the selected item of the spinner
        if (selectedIndex != -1) {
            spinner.setSelection(selectedIndex)
        }

    }

    private fun resetLayout(){
        binding.profileMainLayout.setBackgroundColor(requireContext().getColor(R.color.PrimaryColor))
        binding.profileTopShape.visibility = View.VISIBLE
        binding.profileDataLayout.visibility = View.VISIBLE
        binding.profileLoadingLotti.visibility = View.GONE
        binding.profileUpdateUserSeuccess.visibility = View.GONE
    }

}