package com.example.trainlivelocation.utli

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Message
import com.example.domain.entity.Post
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.PostModelResponse
import com.example.trainlivelocation.databinding.ChatMessageItemLayoutBinding
import com.example.trainlivelocation.databinding.InboxMessageItemLayoutBinding
import com.example.trainlivelocation.databinding.PostCommentItemLayoutBinding
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class MessageAdapterViewHolder(
    private val binding: ChatMessageItemLayoutBinding,
    private val MessageListener: MessageListener,
    private val phone: String,
    private val layoutType:String
    ) : RecyclerView.ViewHolder(binding.root) {
    private val TAG:String?="MessageAdapterViewHolder"
    fun bind(message: Message) {
        when (layoutType) {
            "message" -> {
                binding.inboxChatLayout.visibility=View.GONE
                binding.message = message
                binding.listener=MessageListener
//                if (message.sender!!.equals(phone)) {
//                    binding.chatMessageItemTextView.setBackgroundColor(Color.parseColor("#0081C9"))
//                    binding.chatMessageItemLayout.layoutDirection = View.LAYOUT_DIRECTION_RTL
//                }


            }
            "inbox" -> {
                binding.listener=MessageListener
                binding.chatMessageItemLayout.visibility=View.GONE
                Log.i(TAG,"${message}")
                binding.message = message
            }
        }
    }


}