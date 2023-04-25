





package com.example.trainlivelocation.ui

import Resource
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.PostModelResponse
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.databinding.FragmentAllPostsBinding
import com.example.trainlivelocation.utli.*


class AllPosts : Fragment() , PostListener, FragmentLifecycle,DeletePostListener {
    private val TAG:String?="AllPostsFragment"
    private var flagFirstTimeRunning:Boolean=false
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
        allPostFragmentViewmodel.getUserDataFromsharedPreference()
        setObserver()
        flagFirstTimeRunning=true
        return binding.root
    }



    override fun onResume() {
        super.onResume()
        binding.adapter=setAdapterItems()
        setObserver()
    }

    override fun onPause() {
        super.onPause()
        binding.adapter=setAdapterItems()
        setObserver()
    }

    private fun setObserver() {
        allPostFragmentViewmodel?.userData!!.observe(viewLifecycleOwner, Observer {
            userModel =it
        })

    }
    private fun setAdapterItems():PostCustomAdapter{
        val adapter= PostCustomAdapter(this)
        allPostFragmentViewmodel.allPosts!!.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    binding.allPostsPostShimmerLayout.setVisibility(View.VISIBLE)
                    binding.allPostsEmptyPostLayout.setVisibility(View.GONE)
                    binding.allPostsRCVPosts.setVisibility(View.GONE)

                }
                is Resource.Success->{

                    if (it.data.isEmpty()){
                        binding.allPostsPostShimmerLayout.setVisibility(View.GONE)
                        binding.allPostsRCVPosts.setVisibility(View.GONE)
                        binding.allPostsEmptyPostLayout.setVisibility(View.VISIBLE)
                        Log.i(TAG,"${it.data}")
                        adapter.setData(it.data!!)
                    }else{
                        binding.allPostsPostShimmerLayout.setVisibility(View.GONE)
                        binding.allPostsEmptyPostLayout.setVisibility(View.GONE)
                        binding.allPostsRCVPosts.setVisibility(View.VISIBLE)
                        Log.i(TAG,"${it.data}")
                        adapter.setData(it.data!!)
                    }
                }
                is Resource.Failure->{
                    binding.allPostsPostShimmerLayout.setVisibility(View.GONE)
                    Log.e(TAG,"${it.error}")
                }
                else -> {

                }
            }
        })
        return adapter
    }

    companion object {
        var userModel: UserResponseItem?=null
    }

    override fun OnCommentClickListener(post: PostModelResponse) {
        var dialog = Add_post_comment(post)
        var childFragmentManager = getChildFragmentManager()
        dialog.show(childFragmentManager, "Add_post_comment")
    }

    override fun OnReportClickListener(post: PostModelResponse) {
        TODO("Not yet implemented")
    }

    override fun OnDeleteClickListener(post: PostModelResponse) {
        if (userModel!!.id==post.userId){
            var dialog = Delete_post_dialog_fragment(post,this)
            var childFragmentManager = getChildFragmentManager()
            dialog.show(childFragmentManager, "Delete_post_dialog_fragment")
        }else{
            toast("You Are not post Owner...")
        }

    }

    override fun OnSettingClickListener(post: PostModelResponse) {
        TODO("Not yet implemented")
    }

    override fun onPauseFragment() {
        if (flagFirstTimeRunning){
            Log.i(TAG,"onPauseFragment")
        }
    }

    override fun onResumeFragment() {
        if (flagFirstTimeRunning){
            allPostFragmentViewmodel.getPosts()
        }
    }

    override fun onPostDeleted(isDeleted: Boolean) {
        if (isDeleted){
            allPostFragmentViewmodel.getPosts()
        }else{
            toast("Failed To Delete The Post")
        }
    }
}