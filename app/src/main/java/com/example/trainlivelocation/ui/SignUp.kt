package com.example.trainlivelocation.ui

import Resource
import android.Manifest
import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.entity.LocationDetails
import com.example.domain.entity.NotificatonToken
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentSignUpBinding
import com.example.trainlivelocation.utli.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.sign


class SignUp : Fragment(), DatePickerListener, Station_Dialog_Listener {

    private val signUpViewModel: SignUpViewModel? by activityViewModels()
    private lateinit var binding: FragmentSignUpBinding
    private val REQUSET_CODE_IMAGE: Int = 101
    private val args by navArgs<SignUpArgs>()
    private val TAG: String? = "Sign_up_Fragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = signUpViewModel
            }
        signUpViewModel?.setbaseActivity(requireActivity() as AppCompatActivity)

        var jobArrayAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.jopsArray,
            R.layout.spinner_jop_item_layout
        )
        jobArrayAdapter.setDropDownViewResource(R.layout.spinner_job_dropdown_item_layout)
        binding.RegisterSpinnerJobs.adapter = jobArrayAdapter
        binding.signUpImageViewProofileImage.setOnClickListener {
            getImageUri()
        }


        setObservers()


        return binding.root
    }

    private fun setObservers() {
        signUpViewModel!!.nextBtnClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Log.i(TAG, "setObservers-nextBtnClicked")

                //generate token for notification
                signUpViewModel!!.generateUserNotificationToken()
                signUpViewModel!!.generateNotificationToken.observe(viewLifecycleOwner, Observer {
                    when(it){

                        is Resource.Loading->{
                            Log.i(TAG,"Waiting for generating notification token...")
                        }
                        is Resource.Success->{
                            Log.i(TAG,"Success  generating notification token...")
                            //send user date to api
                            signUpViewModel!!.sendUserDataToApi("${args.userPhone}", stationIdNumber,it.data!!)
                            signUpViewModel!!.userDataLive.observe(viewLifecycleOwner, Observer {
                                when (it) {
                                    is Resource.Loading -> {
                                        binding.signUpLocationProgressBar.setVisibility(View.VISIBLE)
                                    }
                                    is Resource.Success -> {
                                        Log.i(TAG, "User Added ${it.data}")
                                        binding.signUpLocationProgressBar.setVisibility(View.GONE)
                                        binding!!.signUpBtnNext.setText("Submit")
                                        binding.signUpDataLayout.setVisibility(View.GONE)
                                        binding.signUpLayoutProfileImage.setVisibility(View.VISIBLE)
                                    }
                                    is Resource.Failure -> {
                                        showCustomToast(requireContext(),it.error!!)
                                        Log.e(TAG, it.error!!)
                                    }
                                    else -> {

                                    }
                                }
                            })
                        }
                        is Resource.Failure->{

                        }
                        else->{

                        }
                    }
                })


            }

        })




        signUpViewModel!!.chooseImageBtnClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                getImageUri()
            }
        })



        signUpViewModel!!.submitBtnClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                if (imageUri!=null){
                    signUpViewModel!!.uploadProfileImage(imageUri!!, "+20${args.userPhone}")
                    signUpViewModel!!.sendingProfileImageResult.observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is Resource.Loading -> {
                                binding.signUpSuccessLoadingLotti.setVisibility(View.GONE)
                                binding.signUpLayoutProfileImage.setVisibility(View.GONE)
                                binding.signUpBtnNext.setVisibility(View.GONE)
                                binding.signUpLoadingLayout.setVisibility(View.VISIBLE)
                            }
                            is Resource.Success -> {
                                Log.i(TAG, "Image Send Successfully")
                                binding.signUpLoadingLotti.setVisibility(View.GONE)
                                binding.signUpSuccessLoadingLotti.setVisibility(View.VISIBLE)
                                binding.signUpSuccessLoadingLotti.playAnimation()
                                binding.signUpSuccessLoadingLotti.addAnimatorListener(object :
                                    AnimatorListener {
                                    override fun onAnimationStart(p0: Animator) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onAnimationEnd(p0: Animator) {
                                        findNavController().navigate(SignUpDirections.actionSignUpToSplash())
                                    }

                                    override fun onAnimationCancel(p0: Animator) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onAnimationRepeat(p0: Animator) {
                                        TODO("Not yet implemented")
                                    }

                                })
                            }
                            is Resource.Failure -> {
                                Log.i(TAG, "Failed To send image to Fire base ${it.error}")
                            }

                            else -> {
                                Log.e(TAG, "else brunch...")
                            }
                        }
                    })
                }else{
                    showCustomToast(requireContext(),"Please choose image")
                }

            }
        })



        signUpViewModel!!.datePickerTxtClicked.observe(viewLifecycleOwner, Observer {
            var dialog = DatePickerDialogFragment(this)
            var childFragmentManager = getChildFragmentManager()
            dialog.show(childFragmentManager, "dialog_datepicker")
        })



        signUpViewModel!!.stationTxtClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                var dialog = ChooseStationDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "station_dialog")
            }
        })


    }


    private fun getImageUri() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            startActivityForResult(it, REQUSET_CODE_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUSET_CODE_IMAGE) {
            data?.data.let {
                imageUri = it
                binding.signUpImageViewProofileImage.setImageURI(imageUri)
            }
        }
    }


    companion object {
        private var imageUri: Uri? = null
        var stationIdNumber: Int? = null
    }

    override fun onDateSelected(date: String) {
        binding.SignUpTxtDatePicker.setText(date)

    }

    override fun onStationSelected(
        StationId: Int?,
        StationName: String?,
        longitude: Double?,
        latitude: Double?
    ) {
        binding.SignUpTxtStation.setText(StationName!!)
        signUpViewModel!!.getAddress(longitude!!, latitude!!)
        stationIdNumber = StationId
    }


}