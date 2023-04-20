package com.example.trainlivelocation.ui

import Resource
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.PostModelResponse
import com.example.domain.entity.userResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentDeletePostDialogFragmentBinding
import com.example.trainlivelocation.utli.DeletePostListener
import com.example.trainlivelocation.utli.toast


class Delete_post_dialog_fragment(var post:PostModelResponse,private val listener:DeletePostListener) : DialogFragment() {
    private val TAG:String?="Delete_post_dialog_fragment"
    private var _binding: FragmentDeletePostDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private val deletePostDialogFragmentViewModel: Delete_post_dialog_fragment_ViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentDeletePostDialogFragmentBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=deletePostDialogFragmentViewModel
            }

        setObserver()

        return binding.root
    }

    private fun setObserver() {
        deletePostDialogFragmentViewModel.btnYesClicked.observe(viewLifecycleOwner, Observer {
            if (it==true){
                deletePostDialogFragmentViewModel.deletePostFromApi(post.id)

                deletePostDialogFragmentViewModel.postDeleted.observe(viewLifecycleOwner, Observer {
                    when(it){
                        is Resource.Loading->{
                            toast("wait for Deleting Post")
                        }
                        is Resource.Failure->{
                            Log.e(TAG,"${it.error}")
                            listener.onPostDeleted(false)
                            dismiss()
                        }
                        is Resource.Success->{
                            listener.onPostDeleted(true)
                            dismiss()
                        }
                        else->{
                            listener.onPostDeleted(false)
                            dismiss()
                        }
                    }
                })
            }
        })



        deletePostDialogFragmentViewModel.btnNoClicked.observe(viewLifecycleOwner, Observer {
            if (it == true){
                dismiss()
            }
        })
    }

    companion object {

    }
}