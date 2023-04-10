package com.example.trainlivelocation.utli

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class PostCustomAdapter(private val postListener: PostListener) :
    RecyclerView.Adapter<PostAdapterViewHolder>(), BindableAdapter<ArrayList<PostModelResponse>> {

    private var binding: UserPostsRcvItemLayoutBinding? = null
    lateinit var postArrayList: ArrayList<PostModelResponse>
    var postList = emptyList<PostModelResponse>()

    public fun updateData(postList: ArrayList<PostModelResponse>) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapterViewHolder {
        binding = UserPostsRcvItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostAdapterViewHolder(binding!!, postListener)
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: PostAdapterViewHolder, position: Int) {
        val post = postList.get(position)
        holder.bind(post)
    }

    override fun setData(data: ArrayList<PostModelResponse>) {
        this.postList = data
        notifyDataSetChanged()
    }
}
