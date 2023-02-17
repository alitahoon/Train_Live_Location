package com.example.trainlivelocation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentLoginBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [signIn.newInstance] factory method to
 * create an instance of this fragment.
 */
class signIn : Fragment(), SignInListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val loginViewModel: UserLoginViewModel? by activityViewModels()
    private lateinit var binding: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = loginViewModel
            }
        binding.viewmodel?.signInListener = this
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment login.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            signIn().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStartLogin() {
        binding.loginProgressBar.setVisibility(View.VISIBLE)

    }

    override fun onSuccessLogin() {
        loginViewModel?.userLoginDataLive?.observe(viewLifecycleOwner,
        Observer {
            if (it!= null){
                binding.loginProgressBar.setVisibility(View.GONE)
                Log.e("login",it.toString())
            }
        })
    }

    override fun onSignUpBtnClicked() {
        findNavController().navigate(R.id.action_sign_in_to_sign_up)
    }

    override fun onLoginFailure(message: String) {
        binding.loginProgressBar.setVisibility(View.GONE)
    }

}
