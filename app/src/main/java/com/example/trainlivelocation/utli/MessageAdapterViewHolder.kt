package com.example.trainlivelocation.utli

import android.graphics.Color
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
    private val binding:ChatMessageItemLayoutBinding,
    private val MessageListener: MessageListener,
    private val phone:String,
    private val bindingInbox:InboxMessageItemLayoutBinding,
    private val layoutType:String
) :RecyclerView.ViewHolder(binding.root){
    fun bind(message:Message) {
       when(layoutType){
           "message"->{
               if (message.sender!!.equals(phone)){
                   binding.chatMessageItemTextView.setBackgroundColor(Color.parseColor("#0081C9"))
                   binding.chatMessageItemLayout.layoutDirection= View.LAYOUT_DIRECTION_RTL
               }
               binding.message = message

           }
           "inbox"->{
               binding.message = message
           }
       }
    }
}