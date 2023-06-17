package com.example.trainlivelocation.ui

import Resource
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.domain.entity.ReportPostResponse
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentReportPostBinding
import com.example.trainlivelocation.utli.getuserModelFromSharedPreferences
import com.example.trainlivelocation.utli.showCustomToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Report_Post(private val post: PostModelResponse) : DialogFragment() {
    private var binding: FragmentReportPostBinding? = null
    private val TAG = "Report_Post"
    private val report_Post_ViewModel: Report_Post_ViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportPostBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = report_Post_ViewModel
            }
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding!!.postReportReason1.setOnClickListener {
            reportReason = binding!!.postReportReason1.text.toString()
            report()
        }

        binding!!.postReportReason2.setOnClickListener {
            reportReason = binding!!.postReportReason2.text.toString()
            report()

        }

        binding!!.postReportReason3.setOnClickListener {
            reportReason = binding!!.postReportReason3.text.toString()
            report()

        }

        binding!!.postReportReason4.setOnClickListener {
            reportReason = binding!!.postReportReason4.text.toString()
            report()

        }


        setObservers()
        return binding!!.root
    }

    private fun setObservers() {
        report_Post_ViewModel.btnYesClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding!!.reportPostBtnCancel.visibility = View.GONE
                binding!!.reportPostBtnYes.visibility = View.GONE
                binding!!.postreportText1.visibility = View.GONE
                binding!!.reportPostChooseReasonLayout.visibility = View.VISIBLE
            }
        })

        report_Post_ViewModel.btnCancelClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                dismiss()
            }
        })
        report_Post_ViewModel.report.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.i(TAG, "reporting post...")
                }
                is Resource.Failure -> {
                    Log.e(TAG, "${it.error}")
                }
                is Resource.Success -> {
                    Log.e(TAG, "${it.data}")
                    showCustomToast(requireContext(), "Post reporting successfully...")
                    dismiss()
                }
                else -> {}
            }
        })
    }

    fun report() {
        //check if the user has report before on this post
        report_Post_ViewModel.getAllReports()
        report_Post_ViewModel.allReports.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Log.i(TAG, "${it.data}")
                    if (it.data.any { it.postId==post.id && it.userId==getuserModelFromSharedPreferences(requireContext()).id}){
                        showCustomToast(requireContext(),"You have Reported this post before..")
                    }else{
                        report_Post_ViewModel.report(
                            post.id, getuserModelFromSharedPreferences(requireContext()).id,
                            reportReason!!
                        )
                    }
                }
                is Resource.Failure -> {
                    Log.e(TAG, "${it.error}")
                }
                is Resource.Loading -> {
                    Log.i(TAG, "getting  reports...")
                }
                else -> {}
            }
        })


    }

    companion object {
        private var reportReason: String? = null
    }
}