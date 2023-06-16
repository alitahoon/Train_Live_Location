package com.example.trainlivelocation.ui

import Resource
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

        var jobArrayAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.jopsArray,
            R.layout.spinner_jop_item_layout
        )
        jobArrayAdapter.setDropDownViewResource(R.layout.spinner_job_dropdown_item_layout)
        binding.profileSpinnerJobs.adapter=jobArrayAdapter

        setSpinnerSelectionByValue(user!!.jop)
        setObserver()
        return binding.root
    }

    private fun setObserver() {

        userProfileViewModel.btnSaveUserDateClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                var user: UserResponseItem?=UserProfileArgs.fromBundle(requireArguments()).userModel
                userProfileViewModel.updateUserProfileData(user!!.id,
                    RegisterUser(
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
                )

                userProfileViewModel.userUpdated.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is Resource.Loading -> {
                            binding.profileMainLayout.setBackgroundColor(requireContext().getColor(R.color.white))
                            binding.profileTopShape.visibility = View.GONE
                            binding.profileDataLayout.visibility = View.GONE
                            binding.profileLoadingLotti.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            Log.i(TAG, "${it.data}")
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
                                    findNavController().navigate(R.id.action_userProfile_to_home2)
                                }

                                override fun onAnimationCancel(p0: android.animation.Animator) {
                                    TODO("Not yet implemented")
                                }

                                override fun onAnimationRepeat(p0: android.animation.Animator) {
                                    TODO("Not yet implemented")
                                }

                            })
                        }
                        is Resource.Failure -> {
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
                                    findNavController().navigate(R.id.action_userProfile_to_home2)
                                }

                                override fun onAnimationCancel(p0: android.animation.Animator) {
                                    TODO("Not yet implemented")
                                }

                                override fun onAnimationRepeat(p0: android.animation.Animator) {
                                    TODO("Not yet implemented")
                                }

                            })
//                            resetLayout()
                            Log.e(TAG, "Failed To Update user Data -> ${it.error}")
                        }
                        else -> {
                            resetLayout()
                            Log.e(TAG, "Failed To Update user Data else brunch")
                        }
                    }
                })
            }
        })

        userProfileViewModel.stationName.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.profileTxtStation.setText(" ")
                    Log.i(TAG, "${it}")
                }
                is Resource.Success -> {
                    binding.profileTxtStation.setText(it.data!!.name)
                    Log.i(TAG, "${it}")
                }
                is Resource.Failure -> {
                    Log.e(TAG, "Error while loading station name from api ---> ${it.error}")
                    toast("Error getting Station Name")
                }
                else -> {
                    Log.e(TAG, "Error while loading station name from api ---> else brunsh")
                }
            }
        })
    }

    fun setSpinnerSelectionByValue(value: String) {
        val xmlArray: Array<String> =
            requireContext().resources.getStringArray(R.array.jopsArray) // get array from resources
        val spinner = binding.profileSpinnerJobs // get the spinner element

        spinner.setSelection(xmlArray.indexOf(
            xmlArray.first { elem -> elem == value } // find first element in array equal to value
        )) // get index of found element and use it as the position to set spinner to.
    }

    private fun resetLayout(){
        binding.profileMainLayout.setBackgroundColor(requireContext().getColor(R.color.PrimaryColor))
        binding.profileTopShape.visibility = View.VISIBLE
        binding.profileDataLayout.visibility = View.VISIBLE
        binding.profileLoadingLotti.visibility = View.GONE
        binding.profileUpdateUserSeuccess.visibility = View.GONE
    }

}