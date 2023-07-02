package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.trainlivelocation.databinding.FragmentCriticalPostBinding
import com.example.trainlivelocation.utli.FragmentLifecycle
import com.example.trainlivelocation.utli.PostCustomAdapter
import com.example.trainlivelocation.utli.PostListener
import com.example.trainlivelocation.utli.toast


class CriticalPost : Fragment() , PostListener ,FragmentLifecycle {

    private lateinit var binding:FragmentCriticalPostBinding
    private var flagFirstTimeRunning:Boolean=false
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
        flagFirstTimeRunning=true

        return binding!!.root
    }

    private fun setAdapterItems():PostCustomAdapter{
        binding.crticalProgressBar.setVisibility(View.VISIBLE)
        val adapter:PostCustomAdapter= PostCustomAdapter(this)
        criticalpostsViewModel?.postLiveData?.observe(viewLifecycleOwner, Observer {
//            adapter.setData(it)
            binding.crticalProgressBar.setVisibility(View.INVISIBLE)


        })
        return adapter
    }

    private fun setObservers() {

    }


    companion object {

    }

    override fun OnCommentClickListener(post: PostModelResponse) {
        TODO("Not yet implemented")
    }

    override fun OnReportClickListener(post: PostModelResponse) {
        TODO("Not yet implemented")
    }

    override fun OnDeleteClickListener(post: PostModelResponse) {
        TODO("Not yet implemented")
    }

    override fun OnSettingClickListener(post: PostModelResponse) {
        TODO("Not yet implemented")
    }

    override fun onPauseFragment() {
        if (flagFirstTimeRunning){
            toast("onPauseFragment")
        }
    }

    override fun onResumeFragment() {
        if (flagFirstTimeRunning){
            toast("onPauseFragment")
        }    }
}