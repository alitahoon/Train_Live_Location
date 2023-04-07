package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentSignInBinding
import com.example.trainlivelocation.utli.SignInListener
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignIn.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SignIn : Fragment(), SignInListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val signInViewModel: SignInViewModel? by viewModels()
    private lateinit var binding: FragmentSignInBinding
    private val TAG:String?="sign_in_Fragment"


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
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = signInViewModel
            }
        binding.viewmodel?.signInListener = this
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        signInViewModel!!.signUpBtnClicked.observe(viewLifecycleOwner, Observer {
            if (it==true){
                findNavController().navigate(SignInDirections.actionSignInToAuthCheck())
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignIn.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignIn().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStartLogin() {
        findNavController().navigate(R.id.action_sign_in_to_mainActivity)
    }

    override fun onSuccessLogin(userPhone:String,userPassowrd:String) {

//
//        signInViewModel?.userLoginDataLive?.observe(viewLifecycleOwner,
//            Observer {
//                if (it!= null){
//                    Log.e("login",it.toString())
//                    if (it.get(0).phone.equals(userPhone) && it.get(0).password.equals(userPassowrd)){
//                        findNavController().navigate(R.id.action_sign_in_to_mainActivity)
//                    }else{
//                        Toast.makeText(requireContext(), "Wrong Phone or Password", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            })
    }

    override fun onSignUpBtnClicked() {
    }
    override fun onLoginFailure(message: String) {
    }
}