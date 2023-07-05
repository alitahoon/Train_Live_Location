package com.example.trainlivelocation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.ActivitySplashLoadingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Splash_Loading : AppCompatActivity() {
    private var binding:ActivitySplashLoadingBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashLoadingBinding.inflate(layoutInflater)

        setContentView(binding!!.root)
        if (supportActionBar != null) {
            supportActionBar?.hide()
        }
    }
}