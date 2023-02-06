package com.example.trainlivelocation.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.ActivitySplashBinding

class splash : AppCompatActivity() {
    //    @Inject
//    lateinit var mainFragemntFactory: MainFragemntFactory
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(getColor(R.color.basicColor))
        }
//        val animationScale = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
//        binding.appLogo.startAnimation(animationScale)
//        supportFragmentManager.fragmentFactory=mainFragemntFactory
        supportFragmentManager.beginTransaction()
            .replace(R.id.splash_fragmentContainer,register(),null)
            .commit()
//        val splashRunnable=Runnable{
//            binding.appLogo.setVisibility(View.GONE)
//            binding.txtLive.setVisibility(View.GONE)
//            binding.txtLogo.setVisibility(View.GONE)
//            binding.splashLayoutAfterLoading.setVisibility(View.VISIBLE)
//        }
//        var hand=Handler()
//        hand.postDelayed(splashRunnable,2000)


    }

//        private fun animateLogo() {
//            binding.txtLogo.translationY = 400f
//            binding.txtLive.translationY = 400f
//            binding.txtLogo.animate()
//                .translationY(0f)
//                .setInterpolator(LinearInterpolator())
//                .setStartDelay(1000)
//                .start()
//            binding.txtLive.animate()
//                .translationY(0f)
//                .setInterpolator(LinearInterpolator())
//                .setStartDelay(1000)
//                .start()
//        }


}