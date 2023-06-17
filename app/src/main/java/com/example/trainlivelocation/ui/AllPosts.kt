





package com.example.trainlivelocation.ui

import Resource
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.AddPostNotificationData
import com.example.domain.entity.PostModelResponse
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.databinding.FragmentAllPostsBinding
import com.example.trainlivelocation.utli.*
import java.util.*
import kotlin.collections.ArrayList


class AllPosts : Fragment() , PostListener, FragmentLifecycle,DeletePostListener {
    private val TAG:String?="AllPostsFragment"
    private var flagFirstTimeRunning:Boolean=false
    private lateinit var binding: FragmentAllPostsBinding
    private lateinit var notificationModel:AddPostNotificationData
    private val allPostFragmentViewmodel : AllPostsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allPostFragmentViewmodel.getPosts()
        arguments?.let {
            notificationModel = it.getSerializable("postNotificationModel") as AddPostNotificationData
            Log.i(TAG,"postNotificationModel ${notificationModel}")
            if (notificationModel != null){
                scrollToLastPost=true
            }
        }

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
        binding.adapter=setAdapterItems()
        userModel=getuserModelFromSharedPreferences(requireContext())
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

                        Log.i(TAG,"${it.data}")
                        adapter.setData(it.data)
                        binding.allPostsPostShimmerLayout.setVisibility(View.GONE)
                        binding.allPostsEmptyPostLayout.setVisibility(View.GONE)
                        binding.allPostsRCVPosts.setVisibility(View.VISIBLE)
//                        if (scrollToLastPost) {
//                            val targetPosition = binding.allPostsRCVPosts.adapter?.itemCount?.let { itemCount ->
//                                if (itemCount > 0) itemCount - 1 else RecyclerView.NO_POSITION
//                            } ?: RecyclerView.NO_POSITION
//
//                            if (targetPosition != RecyclerView.NO_POSITION) {
//                                Handler().postDelayed({
//                                    binding.allPostsRCVPosts.smoothScrollToPosition(targetPosition)
//                                }, 3000) // Delay for 1 second (adjust the delay as needed)
//                            }
//                        }
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
        var scrollToLastPost:Boolean=false
        fun newInstance(myObject: AddPostNotificationData): AllPosts {
            val fragment = AllPosts()
            val args = Bundle()
            args.putSerializable("postNotificationModel", myObject)
            fragment.arguments = args
            return fragment
        }
    }

    override fun OnCommentClickListener(post: PostModelResponse) {
        var dialog = Add_post_comment(post)
        var childFragmentManager = getChildFragmentManager()
        dialog.show(childFragmentManager, "Add_post_comment")
    }

    override fun OnReportClickListener(post: PostModelResponse) {
        var dialog = Report_Post(post)
        var childFragmentManager = getChildFragmentManager()
        dialog.show(childFragmentManager, "Report_Post")
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