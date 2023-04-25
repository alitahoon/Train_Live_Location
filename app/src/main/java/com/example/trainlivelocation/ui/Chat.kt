package com.example.trainlivelocation.ui

import Resource
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.entity.Message
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.databinding.FragmentChatBinding
import com.example.trainlivelocation.utli.MessageCustomAdapter
import com.example.trainlivelocation.utli.MessageListener
import com.example.trainlivelocation.utli.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Chat(val reciver:String?,val reciverUserName:String?,val user:UserResponseItem) : BottomSheetDialogFragment(),MessageListener {
    private val TAG: String? = "Chat"
    private lateinit var binding: FragmentChatBinding
    private val chatViewmodel: ChatViewmodel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
            val window: Window? = dialog.window
            if (window != null) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentChatBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=chatViewmodel
                this.reciverPhone=reciver
                this.recivername=reciverUserName
            }
        setObservers()
        Log.i(TAG,"${user}")
        chatViewmodel.getChat(user.phone,reciver)
        binding.adapter=setAdapterItems()
        scrollToLastMessage()
        return binding.root
    }

    private fun setObservers() {


//        chatViewmodel.chatMessages.observe(viewLifecycleOwner, Observer {
//            when(it){
//                is Resource.Loading->{
//                    toast("loading chat Please wait...")
//                }
//                is Resource.Success->{
//                    Log.i(TAG,"${it.data}")
//                }
//                is Resource.Failure->{
//                    Log.e(TAG,"${it.error}")
//                }
//                else -> {
//                    Log.e(TAG,"chatMessages ---> else brunch ")
//                }
//            }
//        })

        chatViewmodel.btnSendMessageClicked.observe(viewLifecycleOwner, Observer {
            if (it==true){
                Log.i(TAG,"${user!!.phone},${reciver}")
                chatViewmodel.sendMessage(user!!.phone,reciver,user.name,reciverUserName)
                chatViewmodel.getChat(user!!.phone,reciver)

                chatViewmodel.messageSend.observe(viewLifecycleOwner, Observer {
                    when(it){
                        is Resource.Loading->{
                            toast("sending message...")
                        }
                        is Resource.Success->{
                            binding.chatTxtMessage.setText("")
                            binding.adapter=setAdapterItems()
                            Log.e(TAG,"${binding.chatRCVMessages.adapter!!.itemCount-1}")
                            binding.chatRCVMessages.layoutManager!!.smoothScrollToPosition(binding.chatRCVMessages,null,binding.chatRCVMessages.adapter!!.itemCount-1)

                        }
                        is Resource.Failure->{
                            Log.e(TAG,"${it.error}")
                        }
                        else -> {

                        }
                    }
                })
            }
        })
    }
    private fun setAdapterItems(): MessageCustomAdapter {
        val adapter= MessageCustomAdapter("message",this, user.phone)
        chatViewmodel.chatMessages.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    Log.i(TAG,"messages is loading ....")
                    binding.chatRCVMessages.setVisibility(View.GONE)
                    binding.chatLoadinigShimmer.setVisibility(View.VISIBLE)
                }
                is Resource.Success->{
                    binding.chatRCVMessages.setVisibility(View.VISIBLE)
                    binding.chatLoadinigShimmer.setVisibility(View.GONE)
                    Log.i(TAG,"data : ${it.data}")
                    adapter.setData(it.data!!)
                }
                is Resource.Failure->{
                    binding.chatLoadinigShimmer.setVisibility(View.VISIBLE)
                    Log.e(TAG,"${it.error}")
                }
                else -> {

                }
            }
        })
        return adapter
    }

    companion object {
    }

    fun scrollToLastMessage(){
        binding.chatRCVMessages.addOnLayoutChangeListener(object :View.OnLayoutChangeListener{
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                if (bottom <= oldBottom) {
                    binding.chatRCVMessages.postDelayed(
                        Runnable { binding.chatRCVMessages.smoothScrollToPosition(bottom) },
                        100
                    )
                }
            }

        })
    }

    override fun OnDeleteClickListener(message: Message) {
        TODO("Not yet implemented")
    }

    override fun OninboxItemClickListener(message: Message) {
        TODO("Not yet implemented")
    }
}