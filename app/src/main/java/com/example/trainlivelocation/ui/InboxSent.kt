package com.example.trainlivelocation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.Message
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentInboxRecieveBinding
import com.example.trainlivelocation.databinding.FragmentInboxSentBinding
import com.example.trainlivelocation.utli.MessageCustomAdapter
import com.example.trainlivelocation.utli.MessageListener
import com.example.trainlivelocation.utli.getuserModelFromSharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class InboxSent : Fragment() ,MessageListener{

    private val TAG: String? = "InboxSent"
    private lateinit var binding: FragmentInboxSentBinding
    private val inboxSentViewModel: InboxSentViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentInboxSentBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=inboxSentViewModel
            }

        inboxSentViewModel.getUserDataFromsharedPreference()
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        inboxSentViewModel.userData!!.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    Log.i(TAG, "loading User data....")
                    binding.inboxChatRCV.setVisibility(View.GONE)
                    binding.inboxLoadinigShimmer.setVisibility(View.VISIBLE)
                }
                is Resource.Success->{
                    user=it.data
                    inboxSentViewModel.getInboxSent(it.data.phone)
                    binding.adapter = setAdapterItems(it.data)
                }
                is Resource.Failure->{
                    Log.i(TAG, "${it.error}")
                }

                else -> {}
            }
        })
    }

    private fun setAdapterItems(userModel:UserResponseItem): MessageCustomAdapter {
        val adapter = MessageCustomAdapter("inbox", this, userModel.phone,getuserModelFromSharedPreferences(requireContext()))
        inboxSentViewModel.inboxSent!!.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.i(TAG, "messages is loading ....")
                    binding.inboxChatRCV.setVisibility(View.GONE)
                    binding.inboxLoadinigShimmer.setVisibility(View.VISIBLE)
                    binding!!.inboxSentImgViewEmptyMsg.setVisibility(View.GONE)
                    binding!!.inboxSentTxtViewEmptyMsg.setVisibility(View.GONE)
                }
                is Resource.Success -> {
                    Log.i(TAG, "data : ${it.data}")

                    if(it.data.isEmpty()){
                        binding!!.inboxChatRCV.visibility = View.GONE
                        binding!!.inboxSentImgViewEmptyMsg.visibility = View.VISIBLE
                        binding!!.inboxSentTxtViewEmptyMsg.visibility = View.VISIBLE
                        binding!!.inboxLoadinigShimmer.visibility = View.GONE
                    }
                    else{
                        binding!!.inboxChatRCV.visibility = View.VISIBLE
                        binding!!.inboxSentImgViewEmptyMsg.visibility = View.GONE
                        binding!!.inboxSentTxtViewEmptyMsg.visibility = View.GONE
                        binding!!.inboxLoadinigShimmer.visibility = View.GONE
                        adapter.setDataWithNoFilter(it.data)
//                        adapter.setData(ArrayList(it.data.filter { it.sender==userModel.phone}.toSet().toList()))

                        if (it.data.size>1){
                            adapter.setData(filterSentMessages(it.data))
                        }else{
                            adapter.setData(it.data)
                        }
                    }
                }
                is Resource.Failure -> {
                    binding.inboxLoadinigShimmer.setVisibility(View.VISIBLE)
                    Log.e(TAG, "${it.error}")
                }
                else -> {

                }
            }
        })
        return adapter
    }
    fun filterSentMessages(inboxList: ArrayList<Message>) :ArrayList<Message>{
        val filterdList= arrayListOf<Message>()
        var oldReciever: String? = inboxList.get(0).sender
        filterdList.add(inboxList.get(0))
        for (i in 1 until  inboxList.size) {
            if (oldReciever == inboxList.get(i).sender) {
                Log.i(TAG,"remove")
            }else{
                filterdList.add(inboxList.get(i))
            }
        }

        Log.i(TAG,"Message List After Filter ${filterdList}")
        return filterdList
    }



    companion object {
        private var user: UserResponseItem? = null
    }

    override fun OnDeleteClickListener(message: Message) {
        TODO("Not yet implemented")
    }

    override fun OninboxItemClickListener(message: Message) {
        var dialog = Chat(message.reciever,message.recieverUserName, user!!)
        var childFragmentManager = getChildFragmentManager()
        dialog.show(childFragmentManager, "Chat")
    }
}