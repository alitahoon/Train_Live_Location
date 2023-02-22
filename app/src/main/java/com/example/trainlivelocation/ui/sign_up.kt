package com.example.trainlivelocation.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.Uri.Builder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private val REQUSET_CODE_IMAGE:Int=101
    private val REQUSET_CODE_Camera:Int=102
    private val REQUSET_CODE_location:Int=103
    private val REQUSET_CODE_background_location:Int=104
    private val REQUSET_CODE_Forground_location:Int=105
    private var imageUri:Uri?=null
    private val TAG:String?="Sign_up_Fragment"


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

        binding.signUpImageViewProofileImage.setOnClickListener{
            //request permission
            checkSelfPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,"IMAGE",REQUSET_CODE_IMAGE)
            checkSelfPermissions(Manifest.permission.CAMERA,"CAMERA",REQUSET_CODE_Camera)
            checkSelfPermissions(Manifest.permission.ACCESS_FINE_LOCATION,"LOCATION",REQUSET_CODE_location)
            checkSelfPermissions(Manifest.permission.ACCESS_BACKGROUND_LOCATION,"BACKGROUND LOCATION",REQUSET_CODE_background_location)
            checkSelfPermissions(Manifest.permission.FOREGROUND_SERVICE,"BACKGROUND LOCATION",REQUSET_CODE_Forground_location)
        }

        return binding.root
    }
    private fun checkSelfPermissions(permissions:String,name:String,requestCode: Int){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(requireActivity(),permissions)==PackageManager.PERMISSION_GRANTED->{
                            Toast.makeText(requireContext(),"$name permissin granted",Toast.LENGTH_SHORT).show()
                        }
                shouldShowRequestPermissionRationale(permissions)-> showDialog(permissions,name,requestCode)
                else-> ActivityCompat.requestPermissions(requireActivity(), arrayOf(permissions),requestCode)
            }
        }
    }

    private fun showDialog(permissions: String, name: String, requestCode: Int) {
        val builder=AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage("Permission to access your $name is required to use this app ")
            setTitle("Permission required")
            setPositiveButton("OK"){dialog,which ->
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permissions),requestCode)
            }
            val dialog=builder.create()
            dialog.show()
        }
    }


    private fun getImageUri(){
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type="image/*"
            startActivityForResult(it,REQUSET_CODE_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK&&resultCode==REQUSET_CODE_IMAGE){
            data?.data.let {
                imageUri=it
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String){
            if (grantResults.isNotEmpty()||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                Toast.makeText(requireContext(),"$name permission Refused",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"$name permission Accepted",Toast.LENGTH_SHORT).show()
                if (requestCode==REQUSET_CODE_IMAGE){
                    getImageUri()
                }
            }
        }
        when (requestCode){
            REQUSET_CODE_IMAGE->innerCheck("Read External Storage")
            REQUSET_CODE_Camera->innerCheck("open Camera")
            REQUSET_CODE_location->innerCheck("Access location ")
            REQUSET_CODE_background_location->innerCheck("Access background location ")
            REQUSET_CODE_Forground_location->innerCheck("Access Forground Services ")
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
        binding.signUpFirstPhaseLayout.setVisibility(View.GONE)
        binding.signUpSecondPhaseLayout.setVisibility(View.VISIBLE)
    }

    override fun onFailure(message: String) {
        Log.e("registerOnFailure",message)
    }


}