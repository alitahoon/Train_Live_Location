package com.example.trainlivelocation.ui

import Resource
import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chaos.view.PinView
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAuthCodeBinding
import com.example.trainlivelocation.databinding.FragmentHomeBinding
import com.example.trainlivelocation.utli.toast
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthCode : Fragment() {
    private val authCodeViewmodel: AuthCodeViewmodel? by activityViewModels()
    private var binding: FragmentAuthCodeBinding? = null
    private var TAG: String? = "AuthCode"
    private val args by navArgs<AuthCodeArgs>()
    private var code: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthCodeBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = authCodeViewmodel
            }
        authCodeViewmodel?.setActivity(requireActivity() as AppCompatActivity)
        binding!!.firstPinView.doOnTextChanged { text, start, before, count ->
            code = text.toString()
            Log.e(TAG, code!!)
        }
        setObservers()
        // Create a CountDownTimer instance
        val timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // This method will be called every second (1000 milliseconds) during the countdown
                val secondsRemaining = millisUntilFinished / 1000
                // Update UI or perform any desired actions with the remaining time
                // For example, update a TextView with the remaining seconds:
                binding!!.resendcodetimer.text = secondsRemaining.toString()+" seconds"
            }

            override fun onFinish() {
                // This method will be called when the countdown is finished
                // Perform any desired actions when the timer finishes
                binding!!.resendcodetimer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                binding!!.resendcodetimer.text = "Resend Code..."
                binding!!.resendcodetimer.isEnabled = true
            }
        }.start()
        return binding!!.root
    }

    private fun setObservers() {
        authCodeViewmodel!!.resendCodeClicked.observe(viewLifecycleOwner, Observer {
            if (it==true){
                authCodeViewmodel!!.resendOtbCode("+20"+args.userPhone)
                authCodeViewmodel!!.resendCode.observe(viewLifecycleOwner, Observer {
                    when(it){
                        is Resource.Loading->{
                            toast("Waiting for resending code...")
                        }
                        is Resource.Success->{
                            toast("Code Resending success...")
                        }
                        is Resource.Failure->{
                            toast("Code Resending Failed...")
                        }
                        else -> {

                        }
                    }
                })
            }
        })
        authCodeViewmodel?.btnSubmitClicked!!.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                //start loading
                authCodeViewmodel?.createPhoneCredential(code)
            }
        })

        authCodeViewmodel?.onCodeSubmitSuccess!!.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Log.i(TAG, "onCodeSubmitSuccess")
                findNavController().navigate(AuthCodeDirections.actionAuthCodeToSignUp(args.userPhone))
            }
        })

        authCodeViewmodel?.onCodeSubmitFailer!!.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding!!.authCodeDialogLoadingCodeSending.setVisibility(View.GONE)
                binding!!.authCodeFirstLayout.setVisibility(View.VISIBLE)
                binding!!.authCodeSecondLayout.setVisibility(View.VISIBLE)
                var snackbar = createSnakeBar(binding!!.root, "wrong code..!")
            }
        })
    }

    fun createSnakeBar(view: View, message: String): Snackbar {
        // create an instance of the snackbar
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)

        // inflate the custom_snackbar_view created previously
        val customSnackView: View =
            layoutInflater.inflate(R.layout.custom_snake_bar_failer, null)

        // set the background of the default snackbar as transparent
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)

        // now change the layout of the snackbar
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

        // set padding of the all corners as 0
        snackbarLayout.setPadding(10, 10, 10, 10)
        return snackbar
    }

    companion object {

    }
}