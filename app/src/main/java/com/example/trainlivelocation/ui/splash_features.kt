package com.example.trainlivelocation.ui

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.trainlivelocation.R

class splash_features : android.app.Fragment() {
    private val REQUSET_CODE_Camera:Int=102
    private val REQUSET_CODE_location:Int=103
    private val REQUSET_CODE_background_location:Int=104
    private val REQUSET_CODE_Forground_location:Int=105
    private val REQUSET_CODE_IMAGE:Int=101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_features, container, false)
    }
    private fun showDialog(permissions: String, name: String, requestCode: Int) {
        val builder= AlertDialog.Builder(activity)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app ")
            setTitle("Permission required")
            setPositiveButton("OK"){dialog,which ->
                ActivityCompat.requestPermissions(activity, arrayOf(permissions),requestCode)
            }
            val dialog=builder.create()
            dialog.show()
        }
    }

    private fun checkSelfPermissions(permissions:String,name:String,requestCode: Int){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(activity,permissions)== PackageManager.PERMISSION_GRANTED->{
                    Toast.makeText(activity,"$name permissin granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permissions)-> showDialog(permissions,name,requestCode)
                else-> ActivityCompat.requestPermissions(activity, arrayOf(permissions),requestCode)
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
                Toast.makeText(activity,"$name permission Refused",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity,"$name permission Accepted",Toast.LENGTH_SHORT).show()
                if (requestCode==REQUSET_CODE_IMAGE){
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
    }
}