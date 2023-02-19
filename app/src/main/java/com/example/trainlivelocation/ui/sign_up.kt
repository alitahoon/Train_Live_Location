package com.example.trainlivelocation.ui

import android.app.Activity
import android.app.Activity.ACTIVITY_SERVICE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentSignUpBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Sign_up.newInstance] factory method to
 * create an instance of this fragment.
 */
class Sign_up : Fragment(),SignUpListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val registerViewModel:UserSignUpViewModel? by activityViewModels()
    private lateinit var binding: FragmentSignUpBinding
    private val REQUEST_CODE:Int?=101
    private var profileImage_URI:Uri?=null
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
        binding= FragmentSignUpBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=registerViewModel
            }
        registerViewModel?.setbaseActivity(requireActivity())
        binding.viewmodel?.userSignUpListener=this

        var jobArrayAdapter:ArrayAdapter<CharSequence> =ArrayAdapter.createFromResource(requireContext(),R.array.jopsArray,R.layout.spinner_jop_item_layout)
        jobArrayAdapter.setDropDownViewResource(R.layout.spinner_job_dropdown_item_layout)
        binding.RegisterSpinnerJobs.adapter=jobArrayAdapter

        //handle get image from gallery
        binding.signUpImageViewProofileImage.setOnClickListener(View.OnClickListener { view ->
            if(VERSION.SDK_INT>=VERSION_CODES.M){
                if (requireActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                    //Permission Denied
                    val permissions= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions,REQUEST_CODE!!)
                }else{
                    //Permission Granted
                    pickImageFromGallery()
                }
                //System os >= marshmello
                pickImageFromGallery()

            }
        })
        return binding.root
    }

    private fun pickImageFromGallery() {
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,REQUEST_CODE!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK&&requestCode==REQUEST_CODE){
            data?.data.let {
                profileImage_URI=it
                binding.signUpImageViewProofileImage.setImageURI(it!!)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CODE->{
                if (grantResults.size>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }else{
                    //Permission Denied
                    Toast.makeText(requireContext(),"Permission Denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment sign_up.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Sign_up().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStartSignUp() {
        Toast.makeText(requireContext(), "onStartRegister", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_sign_up_to_otb_code_verfication)
    }

    override fun onSuccessSignUp() {
        //start dialog otb loading fragment
        //get user post object
//        registerViewModel?.userDataLive?.observe(viewLifecycleOwner,
//        Observer {
//            if (it != null){
//                binding.registerProgressBar.setVisibility(View.GONE)
//                Toast.makeText(requireContext(), "done", Toast.LENGTH_SHORT).show()
//                Log.e("register",it.toString())
//            }
//        })
        }

    override fun onOtbCodeSendToUser() {
    }

    override fun onVerificationCompleted() {
    }

    override fun onVerficationSuccess() {
        TODO("Not yet implemented")
    }

    override fun nextBtnClicked() {
//        binding.signUpFirstPhaseLayout.setVisibility(View.GONE)
//        binding.signUpSecondPhaseLayout.setVisibility(View.VISIBLE)
//        registerViewModel?.uploadProfileImage(profileImage_URI!!)
    }

    override fun onFailure(message: String) {
        Log.e("registerOnFailure",message)
    }


}