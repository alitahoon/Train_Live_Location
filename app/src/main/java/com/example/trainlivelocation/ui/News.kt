package com.example.trainlivelocation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.GetNewsResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentHomeBinding
import com.example.trainlivelocation.databinding.FragmentNewsBinding
import com.example.trainlivelocation.utli.NewsCustomAdapter
import com.example.trainlivelocation.utli.NewsListener
import com.example.trainlivelocation.utli.PostCustomAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class News : Fragment(),NewsListener {

    private val TAG: String? = "News";
    private val newsViewModel: NewsViewModel? by activityViewModels()
    private var binding: FragmentNewsBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsViewModel!!.getNews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentNewsBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=newsViewModel
            }

        binding!!.adapter=setAdapterItems()

       return binding!!.root
    }

    private fun setAdapterItems(): NewsCustomAdapter {
        val adapter= NewsCustomAdapter(this)
        newsViewModel!!.news!!.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    Log.i(TAG,"getting news")
                }
                is Resource.Success->{
                    binding!!.newsShimmer.setVisibility(View.GONE)
                    binding!!.newsRcv.setVisibility(View.VISIBLE)
                    Log.i(TAG,"${it.data}")
                    if (it.data.isEmpty()){

                    }else{
                        adapter.setData(it.data)
                    }
                }
                is Resource.Failure->{
                    binding!!.newsShimmer.setVisibility(View.GONE)
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

    override fun OnNewsClickListener(news: GetNewsResponseItem) {
        var dialog = NewsItemDialog(news)
        var childFragmentManager = getChildFragmentManager()
        dialog.show(childFragmentManager, "NewsItemDialog")
    }
}