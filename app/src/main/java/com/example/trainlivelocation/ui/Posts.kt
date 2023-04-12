package com.example.trainlivelocation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentPostsBinding
import com.example.trainlivelocation.databinding.PostCustomTabBinding
import com.google.android.material.tabs.TabLayoutMediator

class Posts : Fragment() {

    private val postsViewModel:PostsViewModel? by activityViewModels()
    private var binding: FragmentPostsBinding? = null
    private lateinit var customLayoutBinding: PostCustomTabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPostsBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=postsViewModel
            }
        customLayoutBinding= PostCustomTabBinding.inflate(layoutInflater)
        createViewPager(binding?.postsTablayoutViewPager!!)
        TabLayoutMediator(binding?.postsTablayoutTabs!!,binding?.postsTablayoutViewPager!!){
            tab, position ->
            when (position){
              0 -> tab.text = "Add Post "
              1 -> tab.text = "All Posts "
              2 -> tab.text = "Critical"

            }
        }.attach()
//        createTabIcons()
        return binding?.root
    }

    private fun createTabIcons() {
        customLayoutBinding.postTabTitle.text = "Add Posts"
        customLayoutBinding.postTabTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.add_post, 0, 0)
        binding?.postsTablayoutTabs?.getTabAt(0)?.setCustomView(customLayoutBinding.root)
        customLayoutBinding.postTabTitle.text = "All Posts"
        customLayoutBinding.postTabTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.all_posts, 0, 0)
        binding?.postsTablayoutTabs?.getTabAt(1)?.setCustomView(customLayoutBinding.root)
        customLayoutBinding.postTabTitle.text = "Critical"
        customLayoutBinding.postTabTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.critical_posts, 0, 0)
        binding?.postsTablayoutTabs?.getTabAt(2)?.setCustomView(customLayoutBinding.root)

    }

    private fun createViewPager(viewPager: ViewPager2) {
        val adapter :ViewPagerAdapter= ViewPagerAdapter(activity)
        adapter.addFrag(Add_post_fragment(),"Add Post")
        adapter.addFrag(AllPosts(),"All Posts")
        adapter.addFrag(CriticalPost(),"Critical")
        viewPager.adapter = adapter
    }

    internal class ViewPagerAdapter(fragment: FragmentActivity?) :
        FragmentStateAdapter(fragment!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        fun addFrag(fragment: Fragment?, title: String?) {
            mFragmentList.add(fragment!!)
            mFragmentTitleList.add(title!!)
        }
        override fun getItemCount(): Int =mFragmentList.size

        override fun createFragment(position: Int): Fragment =mFragmentList.get(position)


    }



    companion object {

    }
}