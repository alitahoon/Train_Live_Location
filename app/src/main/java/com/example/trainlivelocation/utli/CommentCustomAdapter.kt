package com.example.trainlivelocation.utli

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Post
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.PostModelResponse
import com.example.trainlivelocation.databinding.PostCommentItemLayoutBinding
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class CommentCustomAdapter(private val commentListener: CommentListener) :
    RecyclerView.Adapter<CommentAdapterViewHolder>(), BindableAdapter<ArrayList<PostCommentsResponseItem>> {

    private var binding: PostCommentItemLayoutBinding? = null
    lateinit var commentArrayList: ArrayList<PostCommentsResponseItem>
    var commentList = emptyList<PostCommentsResponseItem>()

    public fun updateData(postList: ArrayList<PostCommentsResponseItem>) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapterViewHolder {
        binding = PostCommentItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentAdapterViewHolder(binding!!, commentListener)
    }

    override fun getItemCount(): Int = commentList.size

    override fun onBindViewHolder(holder: CommentAdapterViewHolder, position: Int) {
        val comment = commentList.get(position)
        holder.bind(comment)
    }

    override fun setData(data: ArrayList<PostCommentsResponseItem>) {
        this.commentList = data
        notifyDataSetChanged()
    }
}
