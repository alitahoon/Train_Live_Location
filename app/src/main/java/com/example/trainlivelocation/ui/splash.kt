package com.example.trainlivelocation.ui

import android.app.Fragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trainlivelocation.R
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage
import dagger.hilt.android.AndroidEntryPoint
import com.example.trainlivelocation.databinding.ActivitySplashBinding


@AndroidEntryPoint
class splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var pop: PaperOnboardingPage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pop= PaperOnboardingPage("Location","gives user the ability to know train location",getColor(
            R.color.PrimaryColor),R.drawable.location_2,R.drawable.location_2)

        val elementList:ArrayList<PaperOnboardingPage> =ArrayList(null)
        elementList.add(pop)

        var onBoardingFragment:PaperOnboardingFragment= PaperOnboardingFragment.newInstance(elementList)

//        onBoardingFragment.setOnRightOutListener {
//            val fragmentTransaction: android.app.FragmentTransaction? = fragmentManager.beginTransaction()
//            val bf: Fragment = splash_features()
//            fragmentTransaction?.replace(R.id.splash_fragment_container, bf!!)
//            fragmentTransaction?.commit()
//        }
    }
}