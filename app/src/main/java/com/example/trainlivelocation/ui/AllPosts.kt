





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
import com.example.domain.entity.Post
import com.example.trainlivelocation.databinding.FragmentAllPostsBinding
import com.example.trainlivelocation.utli.PostCustomAdapter
import com.example.trainlivelocation.utli.PostListener


class AllPosts : Fragment() , PostListener {
    private val TAG:String?="AllPostsFragment"
    private lateinit var binding: FragmentAllPostsBinding
    private val allPostFragmentViewmodel : AllPostsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAllPostsBinding.inflate(inflater,container,false)
            .apply {
                this.viemodel=allPostFragmentViewmodel
            }
        Log.i(TAG,"from all posts")
        allPostFragmentViewmodel.getPosts()
        binding.adapter=setAdapterItems()
        setObserver()
        return binding.root
    }

    private fun setObserver() {
    }
    private fun setAdapterItems():PostCustomAdapter{
        val adapter= PostCustomAdapter(this)
        allPostFragmentViewmodel.allPosts!!.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    binding.allPostsPostShimmerLayout.setVisibility(View.VISIBLE)
                }
                is Resource.Success->{
                    Log.i(TAG,"${it.data}")
                    adapter.setData(it.data!!)
                }
                is Resource.Failure->{
                    Log.e(TAG,"${it.error}")
                }
                else -> {

                }
            }
        })
        return adapter
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