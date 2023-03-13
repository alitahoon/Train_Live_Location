package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.entity.Post
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentCriticalPostBinding
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding


class critical_post : Fragment() {

    private lateinit var binding:FragmentCriticalPostBinding
    private val postsViewModel:CriticalPostViewModel? by activityViewModels()
    private val listener : PostListener? by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCriticalPostBinding.inflate(inflater,container,false)
            .apply {
                this.viewModel = postsViewModel
                this.adapter = Posts_RCV_Adapter(arrayListOf(),listener!!,R.layout.fragment_critical_post)
            }
        return binding!!.root
    }
    @BindingAdapter("setAdapter")
    fun setAdapter(recyclerView: RecyclerView,adapter: PostBaseAdapter<UserPostsRcvItemLayoutBinding,Post>){
        adapter?.let {
            recyclerView.adapter = it
        }
    }

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("submitList")
    fun submitList(recyclerView: RecyclerView, postArrayList:ArrayList<Post>){
        val adapter = recyclerView.adapter as PostBaseAdapter<UserPostsRcvItemLayoutBinding,Post>
        adapter?.updateData(postArrayList?: arrayListOf())
    }

    @BindingAdapter("setImage")
    fun setImage(imageView: ImageView,urlString: String){
        Glide.with(imageView.context).load(urlString).into(imageView)
    }

    @BindingAdapter("onClickReport")
    fun onClickReport(item:Post, isClicked:Boolean){
        if(isClicked){
            // TODO: sent report to API => Fatema
        }
    }

    companion object {

    }
}