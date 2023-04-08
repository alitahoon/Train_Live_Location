package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.Post
import com.example.trainlivelocation.databinding.FragmentCriticalPostBinding
import com.example.trainlivelocation.utli.PostCustomAdapter
import com.example.trainlivelocation.utli.PostListener


class CriticalPost : Fragment() , PostListener {

    private lateinit var binding:FragmentCriticalPostBinding
    private val criticalpostsViewModel:CriticalPostViewModel? by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCriticalPostBinding.inflate(inflater,container,false)
            .apply {
                this.viemodel = criticalpostsViewModel
            }
        binding.adapter=setAdapterItems()

        setObservers()

        return binding!!.root
    }

    private fun setAdapterItems():PostCustomAdapter{
        binding.crticalProgressBar.setVisibility(View.VISIBLE)
        val adapter:PostCustomAdapter= PostCustomAdapter(this)
        criticalpostsViewModel?.postLiveData?.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            binding.crticalProgressBar.setVisibility(View.INVISIBLE)
        })
        return adapter
    }

    private fun setObservers() {

    }


    companion object {

    }

    override fun OnCommentClickListener(post: Post) {
        TODO("Not yet implemented")
    }

    override fun OnReportClickListener(post: Post) {
        TODO("Not yet implemented")
    }

    override fun OnDeleteClickListener(post: Post) {
        TODO("Not yet implemented")
    }

    override fun OnSettingClickListener(post: Post) {
        TODO("Not yet implemented")
    }
}