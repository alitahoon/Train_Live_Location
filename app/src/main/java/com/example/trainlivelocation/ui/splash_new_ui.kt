package com.example.trainlivelocation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.ActivitySplashNewUiBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class splash_new_ui : AppCompatActivity() {
    private lateinit var binding:ActivitySplashNewUiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashNewUiBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}