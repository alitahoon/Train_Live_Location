package com.example.trainlivelocation.ui

import PostsMainSectionsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.example.trainlivelocation.databinding.FragmentPostsBinding
import com.google.android.material.tabs.TabLayout


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Posts.newInstance] factory method to
 * create an instance of this fragment.
 */
class Posts : Fragment() {

    private val postsViewModel:PostsViewModel? by activityViewModels()
    private var binding: FragmentPostsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPostsBinding.inflate(inflater,container,false)
        setupTabs()

        return binding?.root
    }

    private fun setupTabs() {

    }

    @BindingAdapter("bind:handler")
    fun bindViewPagerAdapter(view: ViewPager, activity: MainActivity) {
        val adapter = PostsMainSectionsAdapter(view.context, activity.supportFragmentManager)
        view.adapter = adapter
    }

    @BindingAdapter("bind:pager")
    fun bindViewPagerTabs(view: TabLayout, pagerView: ViewPager?) {
        view.setupWithViewPager(pagerView, true)
    }

    companion object {

    }
}