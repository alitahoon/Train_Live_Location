package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.databinding.FragmentTrainConvertersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainConverters : Fragment() {

    private val TAG: String? = "TrainConverters"
    private lateinit var binding: FragmentTrainConvertersBinding
    private val trainConvertersViewModel: TrainConvertersViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTrainConvertersBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=trainConvertersViewModel
            }
        setObservers()
       return binding.root
    }

    private fun setObservers() {

    }

    companion object {

    }
}