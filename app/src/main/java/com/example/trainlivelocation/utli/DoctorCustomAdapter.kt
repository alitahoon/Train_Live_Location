package com.example.trainlivelocation.utli

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Message
import com.example.domain.entity.Post
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.PostModelResponse
import com.example.trainlivelocation.databinding.ChatMessageItemLayoutBinding
import com.example.trainlivelocation.databinding.InboxMessageItemLayoutBinding
import com.example.trainlivelocation.databinding.PostCommentItemLayoutBinding
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class DoctorCustomAdapter(
    private val layoutType: String?,
    private val doctorListener: DoctorListener
) :
    RecyclerView.Adapter<MessageAdapterViewHolder>(), BindableAdapter<ArrayList<Message>> {
    private val TAG: String? = "MessageCustomAdapter"
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

        return MessageAdapterViewHolder(binding!!, doctorListener, phone,layoutType!!)
    }

    override fun getItemCount(): Int = messageArrayList.size

    override fun onBindViewHolder(holder: MessageAdapterViewHolder, position: Int) {
        val message = messageArrayList.get(position)
        if (layoutType.equals("message") && phone.equals(message.sender)){
            binding!!.chatMessageItemTextView.setBackgroundColor(Color.parseColor("#0081C9"))
            binding!!.chatMessageItemLayout.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }
        holder.bind(message)

    }

    override fun setData(data: ArrayList<Message>) {
        Log.i(TAG, "data from adapter ---> ${data}")
        this.messageArrayList = data
        notifyDataSetChanged()
    }
}
