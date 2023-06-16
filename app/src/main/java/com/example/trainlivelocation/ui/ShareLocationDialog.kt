package com.example.trainlivelocation.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Property
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.databinding.FragmentShareLocationDialogBinding
import com.example.trainlivelocation.utli.*

class ShareLocationDialog : DialogFragment(), Train_Dialog_Listener {
    private val TAG: String? = "ShareLocationDialog"
    private lateinit var binding: FragmentShareLocationDialogBinding
    private val shareLocationDialogViewModel: ShareLocationDialogViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareLocationDialogBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = shareLocationDialogViewModel
            }
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setObservers()
        return binding.root
    }

    private fun setObservers() {
        shareLocationDialogViewModel.btnYes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                toast("Clicked")
                // Create a ValueAnimator to animate the text change
                val animator = ValueAnimator.ofObject(TypeConverter(), "Please Choose train Id")
                animator.duration = 500 // Set the duration of the animation in milliseconds
                animator.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as String
                    binding.shareLocationDialogQuestionText.text = animatedValue
                }
                animator.start() // Start the animation
                binding.shareLocationDialogBtnYes.visibility=View.GONE
                binding.shareLocationDialogBtnNo.visibility=View.GONE
                binding.shareLocationDialogTxtTrainId.visibility=View.VISIBLE
            }
        })

        shareLocationDialogViewModel.btnNo.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                dismiss()
                findNavController().navigate(R.id.action_shareLocationDialog_to_mainActivity)
            }
        })

        shareLocationDialogViewModel.txtTrainIdClicked.observe(viewLifecycleOwner, Observer {
            var dialog = ChooseTrainDialogFragment(this)
            var childFragmentManager = getChildFragmentManager()
            dialog.show(childFragmentManager, "ChooseTrainDialogFragment")
        })
    }

    companion object {

    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        insertUserCurrantTrainIntoSharedPrefrences(requireContext(),trainId!!)
        var locationbckgroundSharingService= Intent(requireActivity(),
            LocationTrackBackgroundService::class.java)
        locationbckgroundSharingService.putExtra("trainId",trainId)
        locationbckgroundSharingService.putExtra("userId",getuserModelFromSharedPreferences(requireContext()).id)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            ContextCompat.startForegroundService(requireContext(),locationbckgroundSharingService!!)
            dismiss()
            findNavController().navigate(R.id.action_shareLocationDialog_to_mainActivity)
        }else{
            requireContext().startService(locationbckgroundSharingService!!)
            findNavController().navigate(R.id.action_shareLocationDialog_to_mainActivity)
            dismiss()

        }
    }
}