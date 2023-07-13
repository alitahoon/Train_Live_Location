package com.example.trainlivelocation.utli

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.*
import com.example.trainlivelocation.databinding.ChatMessageItemLayoutBinding
import com.example.trainlivelocation.databinding.InboxMessageItemLayoutBinding
import com.example.trainlivelocation.databinding.PostCommentItemLayoutBinding
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class MessageAdapterViewHolder(
    private val binding: ChatMessageItemLayoutBinding,
    private val MessageListener: MessageListener,
    private val phone: String,
    private val currantUserModel:UserResponseItem,
    private val layoutType:String,
    private val messagesList:ArrayList<Message>
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
                binding.lastMessage=messagesList[messagesList.size-1].message
                binding.currantUserModel=currantUserModel
            }
        }
    }


}