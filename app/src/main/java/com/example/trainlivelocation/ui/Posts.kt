package com.example.trainlivelocation.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.domain.entity.AddPostNotificationData
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentPostsBinding
import com.example.trainlivelocation.databinding.PostCustomTabBinding
import com.example.trainlivelocation.utli.FragmentLifecycle
import com.example.trainlivelocation.utli.HomeMapListener
import com.example.trainlivelocation.utli.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class Posts : Fragment() {
    private val TAG:String?="Posts"
    private var listener: HomeMapListener? = null
    private val args: PostsArgs by navArgs()
    private val postsViewModel:PostsViewModel? by activityViewModels()
    private var binding: FragmentPostsBinding? = null
    private lateinit var customLayoutBinding: PostCustomTabBinding
    var adapter :ViewPagerAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listener!!.onMoveFromHome()

        // Inflate the layout for this fragment
        binding= FragmentPostsBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=postsViewModel
            }
        binding!!.postsTablayoutViewPager.isUserInputEnabled=false
        customLayoutBinding= PostCustomTabBinding.inflate(layoutInflater)
        if (args.postNotificationModel != null){
            Log.i(TAG,"Post Id : ${args.postNotificationModel!!.postId}")
            createViewPager(binding?.postsTablayoutViewPager!!,args.postNotificationModel)

        }else{
            createViewPager(binding?.postsTablayoutViewPager!!,null)
            Log.i(TAG,"Post Id null")
        }
        TabLayoutMediator(binding?.postsTablayoutTabs!!,binding?.postsTablayoutViewPager!!){
            tab, position ->
            when (position){
              0 -> tab.text = "All Posts"
                1 -> tab.text = "Add Post"
              2 -> tab.text = "Critical"

            }
        }.attach()



        binding!!.postsTablayoutViewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(newPosition: Int) {
                super.onPageSelected(newPosition)
                var currentPosition=0
                var fragmentToShow = adapter!!.createFragment(newPosition) as FragmentLifecycle
                fragmentToShow.onResumeFragment()
                var fragmentToHide = adapter!!.createFragment(currentPosition) as FragmentLifecycle
                fragmentToHide.onPauseFragment()

                currentPosition = newPosition
            }
        })

//        createTabIcons()
        return binding?.root
    }

    private fun createTabIcons() {
        customLayoutBinding.postTabTitle.text = "All Posts"
        customLayoutBinding.postTabTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.all_posts, 0, 0)
        binding?.postsTablayoutTabs?.getTabAt(0)?.setCustomView(customLayoutBinding.root)
        customLayoutBinding.postTabTitle.text = "Add Posts"
        customLayoutBinding.postTabTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.add_post, 0, 0)
        binding?.postsTablayoutTabs?.getTabAt(1)?.setCustomView(customLayoutBinding.root)
        customLayoutBinding.postTabTitle.text = "Critical"
        customLayoutBinding.postTabTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.critical_posts, 0, 0)
        binding?.postsTablayoutTabs?.getTabAt(2)?.setCustomView(customLayoutBinding.root)

    }

    private fun createViewPager(viewPager: ViewPager2,notification:AddPostNotificationData?) {
        adapter= ViewPagerAdapter(activity)
        if (notification != null){
            adapter!!.addFrag(AllPosts.newInstance(notification),"All Posts")
        }else{
            adapter!!.addFrag(AllPosts(),"All Posts")
        }
        adapter!!.addFrag(Add_post_fragment(),"Add Post")
        adapter!!.addFrag(CriticalPost(),"Critical")
        viewPager.adapter = adapter
    }

//    class ViewPagerAdapter(fragment: FragmentActivity?) :
//        FragmentStateAdapter(fragment!!) {
//        private val mFragmentList: MutableList<Fragment> = ArrayList()
//        private val mFragmentTitleList: MutableList<String> = ArrayList()
//        fun addFrag(fragment: Fragment?, title: String?) {
//            mFragmentList.add(fragment!!)
//            mFragmentTitleList.add(title!!)
//        }
//
//        override fun getItemCount(): Int =mFragmentList.size
//
//        override fun createFragment(position: Int): Fragment =mFragmentList.get(position)
//
//
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeMapListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement ActionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener=null
    }

    companion object {

    }
}