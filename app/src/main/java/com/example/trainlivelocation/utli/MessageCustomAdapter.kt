package com.example.trainlivelocation.utli

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Message
import com.example.domain.entity.Post
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.PostModelResponse
import com.example.trainlivelocation.databinding.ChatMessageItemLayoutBinding
import com.example.trainlivelocation.databinding.PostCommentItemLayoutBinding
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class MessageCustomAdapter(private val messageListener: MessageListener,private val phone:String) :
    RecyclerView.Adapter<MessageAdapterViewHolder>(), BindableAdapter<ArrayList<Message>> {
    private val TAG:String?="MessageCustomAdapter"
    private var binding: ChatMessageItemLayoutBinding? = null
    lateinit var messageArrayList: ArrayList<Message>
    var messageList = emptyList<Message>()

    public fun updateData(postList: ArrayList<PostCommentsResponseItem>) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapterViewHolder {
        binding = ChatMessageItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageAdapterViewHolder(binding!!, messageListener,phone)
    }

    override fun getItemCount(): Int = messageArrayList.size

    override fun onBindViewHolder(holder: MessageAdapterViewHolder, position: Int) {
        val comment = messageArrayList.get(position)
        holder.bind(comment)
    }

    override fun setData(data: ArrayList<Message>) {
        Log.i(TAG,"data from adapter ---> ${data}")
        this.messageArrayList = data
        notifyDataSetChanged()
    }
}
