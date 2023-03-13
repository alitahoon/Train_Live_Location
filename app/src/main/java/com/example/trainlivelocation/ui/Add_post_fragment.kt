package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostFragmentBinding

class Add_post_fragment : Fragment() {

    private lateinit var binding: FragmentAddPostFragmentBinding
    private val addPostFragmentViewmodel : Add_post_fragment_ViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostFragmentBinding.inflate(inflater, container, false)
            .apply {
                this.viewModel = addPostFragmentViewmodel
            }
        return binding.root
    }



    companion object {

    }
}