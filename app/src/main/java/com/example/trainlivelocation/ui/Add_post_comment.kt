package com.example.trainlivelocation.ui

import Resource
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.CommentRequest
import com.example.domain.entity.PostModelResponse
import com.example.domain.entity.userResponseItem
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.utli.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class Add_post_comment(var post:PostModelResponse) : BottomSheetDialogFragment() {

    private val TAG: String? = "Add_post_comment"
    private lateinit var binding: FragmentAddPostCommentBinding
    private val addPostCommentFragmentViewModel: Add_post_comment_viewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostCommentBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = addPostCommentFragmentViewModel
            }
        addPostCommentFragmentViewModel.getUserDataFromsharedPreference()
        setObservers()

        return binding.root
    }

    private fun setObservers() {
        addPostCommentFragmentViewModel.btnSendCommentClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Log.i(TAG,"btnSendCommentClicked")
                addPostCommentFragmentViewModel.sendPostCommentToApi(
                    CommentRequest(
                        null,
                        binding.addPostTxtComment.text.toString(),
                        "no image",
                        post.id,
                        userModel!!.id,
                        userModel!!.name,
                        userModel!!.phone
                        )
                )

                addPostCommentFragmentViewModel.postComment!!.observe(viewLifecycleOwner, Observer {
                    when(it){
                        is Resource.Loading->{
                            toast("Sending Comment...")
                        }
                        is Resource.Success->{
//                            binding.addPostRCVComment.visibility=View.INVISIBLE
//                            binding.addPostCommentShimmer.visibility=View.VISIBLE
                            toast("Comment send Successfully")
                            binding.addPostTxtComment.setText("")
                            Log.i(TAG,"${it.data}")
                        }
                        is Resource.Failure->{
                            toast("Comment send Failure")
                            Log.e(TAG,"${it.error}")
                        }
                        else -> {
                            Log.e(TAG,"postComment else brunch....")
                        }
                    }
                })
            }
        })

        addPostCommentFragmentViewModel.userData!!.observe(viewLifecycleOwner, Observer {
            userModel = it
        })
    }

    companion object {
        var userModel: userResponseItem? = null
    }
}