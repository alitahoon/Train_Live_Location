package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.databinding.FragmentAddPostFragmentBinding
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class Add_post_comment : Fragment() {

    private val TAG: String? = "Add_post_comment"
    private lateinit var binding: FragmentAddPostCommentBinding
    private val addPostFragmentViewmodel: Add_post_fragment_ViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddPostCommentBinding.inflate(inflater,container,false)
            .apply {
                this
            }
        return inflater.inflate(R.layout.fragment_add_post_comment, container, false)
    }

    companion object {

    }
}