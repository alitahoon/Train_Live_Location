package com.example.trainlivelocation.ui

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.domain.entity.userResponse
import com.example.domain.entity.userResponseItem
import com.example.trainlivelocation.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Register.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class Register : Fragment() ,RegisterListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val registerViewModel:UserRegisterViewModel? by activityViewModels()
    private lateinit var binding: FragmentRegisterBinding

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
        binding= FragmentRegisterBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=registerViewModel
            }
        registerViewModel?.setbaseActivity(requireActivity())
        binding.viewmodel?.userRegisterListener=this
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment register.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Register().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStartRegister() {
        Toast.makeText(requireContext(), "onStartRegister", Toast.LENGTH_SHORT).show()
        binding.registerProgressBar.setVisibility(View.VISIBLE)
    }

    override fun onSuccessRegister() {
        registerViewModel?.userDataLive?.observe(viewLifecycleOwner,
        Observer {
            if (it != null){
                binding.registerProgressBar.setVisibility(View.GONE)
                Toast.makeText(requireContext(), "done", Toast.LENGTH_SHORT).show()
                Log.e("register",it.toString())
            }
        })
    }

    override fun onFailure(message: String) {
        binding.registerProgressBar.setVisibility(View.GONE)
        Log.e("registerOnFailure",message)
    }
}