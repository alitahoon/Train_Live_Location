package com.example.trainlivelocation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.domain.entity.Message
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.databinding.FragmentInboxBinding
import com.example.trainlivelocation.databinding.PostCustomTabBinding
import com.example.trainlivelocation.utli.FragmentLifecycle
import com.example.trainlivelocation.utli.MessageCustomAdapter
import com.example.trainlivelocation.utli.MessageListener
import com.example.trainlivelocation.utli.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class Inbox() : Fragment() {
    private val TAG: String? = "Inbox"
    private lateinit var binding: FragmentInboxBinding
    private val inboxViewModel: InboxViewModel by activityViewModels()
    private lateinit var customLayoutBinding: PostCustomTabBinding
    var adapter :ViewPagerAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var userModel: UserResponseItem? = InboxArgs.fromBundle(requireArguments()).userModel
        binding = FragmentInboxBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = inboxViewModel
            }
        binding!!.inboxTablayoutViewPager.isUserInputEnabled=false
        customLayoutBinding= PostCustomTabBinding.inflate(layoutInflater)
        createViewPager(binding?.inboxTablayoutViewPager!!)
        TabLayoutMediator(binding?.inboxTablayoutTabs!!,binding?.inboxTablayoutViewPager!!){
                tab, position ->
            when (position){
                0 -> tab.text = "Sent"
                1 -> tab.text = "Received"
            }
        }.attach()



        return binding.root
    }

    private fun setObserver() {

    }
    private fun createTabIcons() {
        customLayoutBinding.postTabTitle.text = "Sent Messages"
        customLayoutBinding.postTabTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.add_post, 0, 0)
        binding.inboxTablayoutTabs.getTabAt(0)?.setCustomView(customLayoutBinding.root)
        customLayoutBinding.postTabTitle.text = "Sent Messages"
        customLayoutBinding.postTabTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.all_posts, 0, 0)
        binding.inboxTablayoutTabs.getTabAt(1)?.setCustomView(customLayoutBinding.root)


    }

    private fun createViewPager(viewPager: ViewPager2) {
        adapter= ViewPagerAdapter(activity)
        adapter!!.addFrag(InboxSent(),"Sent")
        adapter!!.addFrag(InboxRecieve(),"Received")
        viewPager.adapter = adapter
    }


    companion object {
    }



}