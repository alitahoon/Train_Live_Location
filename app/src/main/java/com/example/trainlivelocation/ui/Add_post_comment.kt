package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.CommentRequest
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.PostModelResponse
import com.example.domain.entity.userResponseItem
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.utli.CommentCustomAdapter
import com.example.trainlivelocation.utli.CommentListener
import com.example.trainlivelocation.utli.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Add_post_comment(var post:PostModelResponse) : BottomSheetDialogFragment() ,CommentListener{

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
        txtCommentFocus()
        addPostCommentFragmentViewModel.getUserDataFromsharedPreference()
        addPostCommentFragmentViewModel.getPostComments(post.id)
        setObservers()
        binding.adapter=setAdapterItems()

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

                addPostCommentFragmentViewModel.uploadComment!!.observe(viewLifecycleOwner, Observer {
                    when(it){
                        is Resource.Loading->{
                            toast("Sending Comment...")
                        }
                        is Resource.Success->{
                            toast("Comment send Successfully")
                            binding.addPostTxtComment.setText("")
                            Log.i(TAG,"${it.data}")
                            addPostCommentFragmentViewModel.getPostComments(post.id)
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
    private fun setAdapterItems(): CommentCustomAdapter {
        val adapter= CommentCustomAdapter(this)
        addPostCommentFragmentViewModel.postComments!!.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    binding.addPostRCVComment.setVisibility(View.INVISIBLE)
                    binding.addPostCommentShimmer.setVisibility(View.VISIBLE)
                }
                is Resource.Success->{
                    binding.addPostRCVComment.setVisibility(View.VISIBLE)
                    binding.addPostCommentShimmer.setVisibility(View.INVISIBLE)
                    Log.i(TAG,"${it.data}")
                    adapter.setData(it.data)
                }
                is Resource.Failure->{
                    binding.addPostRCVComment.setVisibility(View.VISIBLE)
                    binding.addPostCommentShimmer.setVisibility(View.INVISIBLE)
                    Log.e(TAG,"${it.error}")
                }
                else -> {

                }
            }
        })
        return adapter
    }

    companion object {
        var userModel: userResponseItem? = null
    }

    override fun OnReportClickListener(post: PostCommentsResponseItem) {
        TODO("Not yet implemented")
    }

    override fun OnDeleteClickListener(post: PostCommentsResponseItem) {
        TODO("Not yet implemented")
    }

    override fun OnChatClickListener(post: PostCommentsResponseItem) {
        toast("chat clicked")
    }
    fun txtCommentFocus(){
        binding.addPostTxtComment.requestFocus()
        binding.addPostTxtComment.setSelection(binding.addPostTxtComment.text.length)
        binding.addPostTxtComment.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm: InputMethodManager? =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.showSoftInput(binding.addPostTxtComment, InputMethodManager.SHOW_IMPLICIT)
            }
        })
    }
}