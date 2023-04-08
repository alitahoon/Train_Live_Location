





package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.domain.entity.Post
import com.example.trainlivelocation.databinding.FragmentAllPostsBinding
import com.example.trainlivelocation.utli.PostCustomAdapter
import com.example.trainlivelocation.utli.PostListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllPosts.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllPosts : Fragment() , PostListener {

    private lateinit var binding: FragmentAllPostsBinding
    private val allPostFragmentViewmodel : AllPostsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAllPostsBinding.inflate(inflater,container,false)
            .apply {
                this.adapter= PostCustomAdapter(this@AllPosts)
                this.viemodel=allPostFragmentViewmodel
            }
        return binding.root
    }

    companion object {

    }

    override fun OnCommentClickListener(post: Post) {
        TODO("Not yet implemented")
    }

    override fun OnReportClickListener(post: Post) {
        TODO("Not yet implemented")
    }

    override fun OnDeleteClickListener(post: Post) {
        TODO("Not yet implemented")
    }

    override fun OnSettingClickListener(post: Post) {
        TODO("Not yet implemented")
    }
}