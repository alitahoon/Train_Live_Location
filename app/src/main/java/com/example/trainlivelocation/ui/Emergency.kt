package com.example.trainlivelocation.ui

import Resource
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.domain.entity.DoctorNotification
import com.example.domain.entity.DoctorResponseItem
import com.example.domain.entity.NotificatonToken
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.databinding.FragmentEmergencyBinding
import com.example.trainlivelocation.utli.*
import com.example.trainlivelocation.utli.constant.Companion.NOTIFICATION_SERVER_KEY

class Emergency : Fragment(), DoctorListener, Train_Dialog_Listener {

    private val TAG: String? = "Emergency"
    private lateinit var binding: FragmentEmergencyBinding
    private val emergencyViewModel: EmergencyViewModel by activityViewModels()
    private val args by navArgs<EmergencyArgs>()

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

        askNotificationPermission()
        setObserver()
        return binding.root
    }


    private fun askNotificationPermission() {
        // Declare the launcher at the top of your Activity/Fragment:
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
                Log.i(TAG, "ok you can send notification..")
            } else {
                Log.i(TAG, " you can not send notification..")
            }
        }
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                Log.i(TAG, "ok you can send notification..")
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setObserver() {
        emergencyViewModel.txtChooseTrainIdClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                //get train inforamtion
                var dialog = ChooseTrainDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseTrainDialogFragment")
            }
        })

//        emergencyViewModel.sentNotification.observe(viewLifecycleOwner, Observer {
//            when (it) {
//                is Resource.Loading -> {
//                    binding.emergancyNotificationProgressBar.visibility = View.VISIBLE
//                }
//                is Resource.Success -> {
//                    binding.emergancyNotificationProgressBar.visibility = View.GONE
//                    displaySnackbarSuccess(
//                        requireContext(),
//                        binding.root,
//                        "Notification Sent Successfully...",
//                        R.raw.notification,
//                        requireActivity().getColor(R.color.DarkPrimaryColor)
//                    )
//                }
//                is Resource.Failure -> {
//                    binding.emergancyNotificationProgressBar.visibility = View.GONE
//                    Log.e(TAG, "${it.error}")
//                }
//
//                else -> {
//                    Log.e(TAG, "Failure from else brunch")
//                }
//            }
//        })
    }

    private fun setAdapterItems(): DoctorCustomAdapter {
        val adapter = DoctorCustomAdapter(this)
        emergencyViewModel.doctors.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.emergancyShimmerLoading.visibility = View.VISIBLE
                    binding.doctorRcv.visibility = View.GONE
                    Log.i(TAG, "messages is loading ....")
                }
                is Resource.Success -> {
                    binding.emergancyShimmerLoading.visibility = View.GONE
                    binding.doctorRcv.visibility = View.VISIBLE
                    Log.i(TAG, "data : ${it.data}")
                    adapter.setData(it.data)

                }
                is Resource.Failure -> {
                    binding.emergancyShimmerLoading.visibility = View.GONE
                    binding.doctorRcv.visibility = View.GONE
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
        emergencyViewModel.getNotificationToken(doctor.userPhone)
        emergencyViewModel.notificationToken.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.i(TAG, "getting notification token.....")
                    binding.emergancyNotificationProgressBar.visibility = View.VISIBLE
                }
                is Resource.Failure -> {
                    Log.e(TAG, "${it.error}")
                    binding.emergancyNotificationProgressBar.visibility = View.GONE
                }
                is Resource.Success -> {
                    if (it != null) {
                        val token = it.data
                        emergencyViewModel.getUserLocation()
                        emergencyViewModel.userLocation.observe(viewLifecycleOwner, Observer {
                            when (it) {
                                is Resource.Loading -> {
                                    Log.i(TAG, "getting user Location .....")
                                }
                                is Resource.Failure -> {
                                    Log.i(TAG, "Failed getting user Location : ${it.error}")
                                    binding.emergancyNotificationProgressBar.visibility = View.GONE
                                }
                                is Resource.Success -> {
                                    if (it != null) {
                                        Log.i(TAG, "success getting user Location : ${it.data}")
                                        //send notification here
                                        emergencyViewModel.sentDoctorNotification(
                                            NotificatonToken(
                                                args.userModel.phone,
                                                args.userModel.name,
                                                token
                                            ),
                                            NOTIFICATION_SERVER_KEY,
                                            DoctorNotification(
                                                "Please help we need doctor",
                                                doctor.userPhone,
                                                doctor.userName,
                                                args.userModel.phone,
                                                args.userModel.name,
                                            ),
                                            Location(it.data)
                                        )
                                        emergencyViewModel.sentNotification.observe(
                                            viewLifecycleOwner,
                                            Observer {
                                                when (it) {
                                                    is Resource.Loading -> {
                                                        Log.i(TAG, "Sending notification...")
                                                    }
                                                    is Resource.Failure -> {
                                                        Log.e(TAG, "${it.error}")
                                                        binding.emergancyNotificationProgressBar.visibility =
                                                            View.GONE

                                                    }
                                                    is Resource.Success -> {
                                                        Log.i(TAG, "${it.data}")
                                                        binding.emergancyNotificationProgressBar.visibility =
                                                            View.GONE
                                                    }
                                                    else -> {

                                                    }
                                                }
                                            })
                                    }

                                }

                                else -> {
                                    binding.emergancyNotificationProgressBar.visibility = View.GONE
                                    Log.e(TAG, "location else brunch")
                                }
                            }
                        })

                    }
                }
                else -> {
                    binding.emergancyNotificationProgressBar.visibility = View.GONE
                    Log.e(TAG, "token else brunch")
                }
            }
        })
    }

    override fun OnChatClickListener(doctor: DoctorResponseItem) {
        if (doctor.userPhone != args.userModel.phone) {
            var dialog = Chat(doctor.userPhone, doctor.userName, args.userModel)
            var childFragmentManager = getChildFragmentManager()
            dialog.show(childFragmentManager, "Chat")
        } else {
            toast("You Can't chat With your self...")
        }

    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        binding.trackLocationTxtTrainId.setText(trainId.toString())
        emergencyViewModel.getDoctors(trainId)
        binding.doctorRcv.adapter = setAdapterItems()
    }
}