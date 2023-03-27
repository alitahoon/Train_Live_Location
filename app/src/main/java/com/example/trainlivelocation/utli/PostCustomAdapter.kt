package com.example.trainlivelocation.utli

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Post
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class PostCustomAdapter(private val postListener: PostListener) :
    RecyclerView.Adapter<PostAdapterViewHolder>(), BindableAdapter<List<Post>> {

    private var binding: UserPostsRcvItemLayoutBinding? = null
    lateinit var postArrayList: ArrayList<Post>
    var postList = emptyList<Post>()

    public fun updateData(postList: ArrayList<Post>) {

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

    override fun setData(data: List<Post>) {
        this.postList = data
        notifyDataSetChanged()
    }
}
interface BindableAdapter<T> {
    fun setData(data: T)
}
