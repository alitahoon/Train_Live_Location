package com.example.trainlivelocation.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentSignUpBinding
import com.example.trainlivelocation.utli.DatePickerListener
import com.example.trainlivelocation.utli.SignUpListener


class SignUp : Fragment(),DatePickerListener {

    private val signUpViewModel:SignUpViewModel? by activityViewModels()
    private lateinit var binding: FragmentSignUpBinding
    private val REQUSET_CODE_IMAGE:Int=101
    private val args by navArgs<SignUpArgs>()


    private var imageUri:Uri?=null

    private val TAG:String?="Sign_up_Fragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSignUpBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=signUpViewModel
            }
        signUpViewModel?.setbaseActivity(requireActivity() as AppCompatActivity)

        var jobArrayAdapter:ArrayAdapter<CharSequence> =ArrayAdapter.createFromResource(requireContext(),R.array.jopsArray,R.layout.spinner_jop_item_layout)
        jobArrayAdapter.setDropDownViewResource(R.layout.spinner_job_dropdown_item_layout)
        binding.RegisterSpinnerJobs.adapter=jobArrayAdapter
        binding.signUpImageViewProofileImage.setOnClickListener{
            getImageUri()
        }


        setObservers()


        return binding.root
    }

    private fun setObservers() {
        signUpViewModel!!.nextBtnClicked.observe(viewLifecycleOwner, Observer {
            if (it == true &&signUpViewModel!!.layoutCounter==0){
                binding.signUpDataLayout.setVisibility(View.GONE)
                Log.i(TAG,"setObservers-nextBtnClicked")
                //send user date to api
                signUpViewModel!!.sendUserDataToApi("+20${args.userPhone}")
                signUpViewModel!!.userDataLive.observe(viewLifecycleOwner, Observer {
                    if (it !=null){
                        Log.i(TAG,"Data sent successfully to api")
                        binding.signUpLayoutProfileImage.setVisibility(View.VISIBLE)
                    }
                })
            }
        })
        signUpViewModel!!.datePickerTxtClicked.observe(viewLifecycleOwner, Observer {
            var dialog = DatePickerDialogFragment(this)
            var childFragmentManager = getChildFragmentManager()
            dialog.show(childFragmentManager,"dialog_datepicker")
        })
    }






    private fun getImageUri(){
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type="image/*"
            startActivityForResult(it,REQUSET_CODE_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK&&requestCode==REQUSET_CODE_IMAGE){
            data?.data.let {
                imageUri=it
            }
        }
    }



    companion object {

    }

    override fun onDateSelected(date: String) {
        binding.SignUpTxtDatePicker.setText(date)

    }


}