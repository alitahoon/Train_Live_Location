package com.example.trainlivelocation.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.Post
import com.example.domain.entity.PostModelResponse
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.InputStream

class Add_post_fragment : Fragment() {
    private val TAG: String? = "Add_post_fragment"
    private lateinit var binding: FragmentAddPostFragmentBinding
    private val addPostFragmentViewmodel: Add_post_fragment_ViewModel by activityViewModels()
    private val REQUSET_CODE_IMAGE: Int? = 108
    private var postImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostFragmentBinding.inflate(inflater, container, false)
            .apply {
                this.viewModel = addPostFragmentViewmodel
            }
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        addPostFragmentViewmodel?.btnSubmitClicked?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                //add image to firebase


                var imageUri = postImageUri
                var imageName = "2"

                addPostFragmentViewmodel?.sendPostImageToFirebase(imageUri!!,"/postImages/")

                // TODO:send imageName to API -> Done
                var critical:Boolean=true
                addPostFragmentViewmodel?.post_redio_checked?.observe(viewLifecycleOwner, Observer {
                    critical=it
                })



//                addPostFragmentViewmodel?.sendPostImageToFirebase(imageUri!!,imageName)
                addPostFragmentViewmodel?.addPost(
                    Post(
                        binding.addPostTxtPostContent.text?.trim().toString(),
                        binding.addPostTxtTrainId.text?.trim().toString().toInt(),
                        critical,
                        imageName,
                        2,
                        "01225137528",
                        "Ali tahoon",
                        null
                    )
                )


            }
        })
        addPostFragmentViewmodel?.btnChooseImageClicked?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                //get image uri
                getImageUri()
                Log.i(TAG, "Add_post_fragment")
            }
        })
    }


    private fun getImageUri() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            startActivityForResult(it, REQUSET_CODE_IMAGE!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUSET_CODE_IMAGE) {
            postImageUri = data?.data
            binding.addPostImageViewPostImage.setImageURI(postImageUri)
        }
    }


    companion object {

    }
}