package com.example.trainlivelocation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Post
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

//class PostsAdapterBinding(
//    private val postArrayList: ArrayList<Post>,
//    private val PostListener: PostListener
//):BaseAdapter<PostViewHolder>() {
//
//
//}

class PostViewHolder<binding:UserPostsRcvItemLayoutBinding>(
   var binder: binding
): RecyclerView.ViewHolder(
    binder.root
) {

}

abstract class PostBaseAdapter<binding:UserPostsRcvItemLayoutBinding,Post>(
    var postArrayList:ArrayList<Post>
) : RecyclerView.Adapter<PostViewHolder<binding>>(){
    @get: LayoutRes
    abstract val layoutId:Int
    abstract fun bind (postBinding:binding,item:Post)
    fun updateData(postArrayList:ArrayList<Post>) {

        this.postArrayList = postArrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder<binding> {

        val binder = DataBindingUtil.inflate<binding>(
            LayoutInflater.from(parent.context), layoutId, parent,false
        )
        return PostViewHolder(binder)
    }

    override fun onBindViewHolder(holder: PostViewHolder<binding>, position: Int) {
        bind(holder.binder,postArrayList[position])
    }

    override fun getItemCount(): Int {
        return postArrayList.size
    }

}