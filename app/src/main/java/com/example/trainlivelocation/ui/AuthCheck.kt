package com.example.trainlivelocation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.ActivityAuthCheckBinding
import com.example.trainlivelocation.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthCheck : AppCompatActivity() {
    private lateinit var binding: ActivityAuthCheckBinding
    private val authCheckViewModel:AuthCheckViewModel? by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthCheckBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}