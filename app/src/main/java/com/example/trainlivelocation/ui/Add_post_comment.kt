package com.example.trainlivelocation.ui

import Resource
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.*
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.utli.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Add_post_comment(var post: PostModelResponse) : BottomSheetDialogFragment(), CommentListener {

    private val TAG: String? = "Add_post_comment"
    private lateinit var binding: FragmentAddPostCommentBinding
    private val addPostCommentFragmentViewModel: Add_post_comment_viewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        val window: Window? = dialog.window
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
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
        userModel=getuserModelFromSharedPreferences(requireContext())
        addPostCommentFragmentViewModel.getPostComments(post.id)
        setObservers()
        binding.adapter = setAdapterItems()

        return binding.root
    }

    private fun setObservers() {
        addPostCommentFragmentViewModel.btnSendCommentClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Log.i(TAG, "btnSendCommentClicked")
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

                addPostCommentFragmentViewModel.uploadComment!!.observe(
                    viewLifecycleOwner,
                    Observer {
                        when (it) {
                            is Resource.Loading -> {
                                toast("Sending Comment...")
                            }
                            is Resource.Success -> {
                                toast("Comment send Successfully")
                                binding.addPostTxtComment.setText("")
                                Log.i(TAG, "${it.data}")
                                addPostCommentFragmentViewModel.getPostComments(post.id)
                                //getToken
                                val usermodel =getuserModelFromSharedPreferences(requireContext())
                                //send notification
                                addPostCommentFragmentViewModel.sendPostCommentNotification(
                                    PushPostCommentNotification(
                                        AddPostCommentNotificationData(
                                            "New Post Comment",
                                            it.data.content,
                                            it.data.adminId,
                                            it.data.content,
                                            post.critical,
                                            post.date,
                                            post.id,
                                            post.img,
                                            post.imgId,
                                            post.trainNumber,
                                            post.userId,
                                            post.userName,
                                            post.userPhone
                                        )
                                    ,usermodel.tokenForNotifications
                                    )
                                )

                            }
                            is Resource.Failure -> {
                                toast("Comment send Failure")
                                Log.e(TAG, "${it.error}")
                            }
                            else -> {
                                Log.e(TAG, "postComment else brunch....")
                            }
                        }
                    })
            }
        })



    }

    private fun setAdapterItems(): CommentCustomAdapter {
        val adapter = CommentCustomAdapter(this)
        addPostCommentFragmentViewModel.postComments!!.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.addCommentLayoutNoComments.setVisibility(View.INVISIBLE)
                    binding.addPostRCVComment.setVisibility(View.INVISIBLE)
                    binding.addPostCommentShimmer.setVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    if (it.data.isEmpty()) {
                        binding.addCommentLayoutNoComments.setVisibility(View.VISIBLE)
                        binding.addPostRCVComment.setVisibility(View.INVISIBLE)
                        binding.addPostCommentShimmer.setVisibility(View.INVISIBLE)
                        Log.i(TAG, "${it.data}")
                        adapter.setData(it.data)

                    } else {
                        binding.addPostRCVComment.setVisibility(View.VISIBLE)
                        binding.addPostCommentShimmer.setVisibility(View.INVISIBLE)
                        binding.addCommentLayoutNoComments.setVisibility(View.INVISIBLE)
                        Log.i(TAG, "${it.data}")
                        adapter.setData(it.data)
                    }
                }
                is Resource.Failure -> {
                    binding.addPostRCVComment.setVisibility(View.VISIBLE)
                    binding.addPostCommentShimmer.setVisibility(View.INVISIBLE)
                    Log.e(TAG, "${it.error}")
                }
                else -> {

                }
            }
        })
        return adapter
    }

    companion object {
        var userModel: UserResponseItem? = null
    }

    override fun OnReportClickListener(comment: PostCommentsResponseItem) {
        TODO("Not yet implemented")
    }

    override fun OnDeleteClickListener(comment: PostCommentsResponseItem) {
        TODO("Not yet implemented")
    }

    override fun OnChatClickListener(comment: PostCommentsResponseItem) {
        if (!comment.userPhone.equals(userModel!!.phone)) {
            var dialog = Chat(comment.userPhone, comment.userName, userModel!!)
            var childFragmentManager = getChildFragmentManager()
            dialog.show(childFragmentManager, "Chat")
        } else {
//            displaySnackbarSuccess(requireContext(), binding.root, "You can't chat with your self")
            toast("You can't chat with your self")
        }

    }

    fun txtCommentFocus() {
        binding.addPostTxtComment.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    val imm =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                }
            }

        })
        binding.addPostTxtComment.requestFocus()
        binding.addPostTxtComment.setSelection(binding.addPostTxtComment.text.length)

    }


}