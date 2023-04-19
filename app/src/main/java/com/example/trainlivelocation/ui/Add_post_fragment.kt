package com.example.trainlivelocation.ui

import Resource
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.Post
import com.example.domain.entity.userResponseItem
import com.example.trainlivelocation.databinding.FragmentAddPostFragmentBinding
import com.example.trainlivelocation.utli.FragmentLifecycle
import com.example.trainlivelocation.utli.toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout


class Add_post_fragment : Fragment(),FragmentLifecycle {
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
        addPostFragmentViewmodel!!.getUserDataFromsharedPreference()
        setObserver()

        return binding.root
    }

    private fun setObserver() {
        addPostFragmentViewmodel?.btnSubmitClicked?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                //add image to firebase
                var imageUri = postImageUri

                var critical: Boolean = true
                addPostFragmentViewmodel?.post_redio_checked?.observe(viewLifecycleOwner, Observer {
                    critical = it
                })

                addPostFragmentViewmodel?.addPost(
                    Post(
                        binding.addPostTxtPostContent.text?.trim().toString(),
                        binding.addPostTxtTrainId.text?.trim().toString().toInt(),
                        critical,
                        "postsImages/${userModel!!.phone}/${userModel!!.id}",
                        userModel!!.id,
                        userModel!!.phone,
                        userModel!!.name,
                        "${userModel!!.id}"
                    )
                )

                addPostFragmentViewmodel!!.sendPostImageToFirebase!!.observe(viewLifecycleOwner, Observer {
                    when(it){
                        is Resource.Loading->{
                            binding.addPostProgressBar.setVisibility(View.VISIBLE)
                        }
                        is Resource.Success->{
                            binding.addPostProgressBar.setVisibility(View.INVISIBLE)
                            getSnakbar(binding.addPostBtnSubmit,com.example.trainlivelocation.R.layout.custom_snake_bar_add_post_success_layout).show()
                        }
                        is Resource.Failure->{
                            binding.addPostProgressBar.setVisibility(View.INVISIBLE)
                            getSnakbar(binding.addPostBtnSubmit,com.example.trainlivelocation.R.layout.custom_snake_bar_add_post_failed_layout).show()
                        }
                        else -> {
                            Log.i(TAG, "Failed else brunch")
                        }
                    }
                })

                addPostFragmentViewmodel!!.post?.observe(viewLifecycleOwner, Observer {
                    when(it){
                        is Resource.Loading->{
                            binding.addPostProgressBar.setVisibility(View.VISIBLE)
                        }
                        is Resource.Success->{
                            Log.e(TAG,"${it}")
                            addPostFragmentViewmodel?.sendPostImageToFirebase(postImageUri!!, "postsImages/${userModel!!.phone}/${it.data.id}")
                        }
                        is Resource.Failure->{
                            Log.e(TAG,"${it.error}")
                            binding.addPostProgressBar.setVisibility(View.INVISIBLE)
                            getSnakbar(binding.addPostBtnSubmit,com.example.trainlivelocation.R.layout.custom_snake_bar_add_post_failed_layout).show()
                        }
                        else -> {
                            binding.addPostProgressBar.setVisibility(View.INVISIBLE)
                            Log.i(TAG, "Failed else brunch")
                        }
                    }
                })

            }
        })


        addPostFragmentViewmodel?.btnChooseImageClicked?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                //get image uri
                getImageUri()
                Log.i(TAG, "Add_post_fragment")
            }
        })

        addPostFragmentViewmodel?.userData!!.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                userModel = it
            } else {
                Log.i(TAG, "Error while getting user shared preferences")
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

    fun getSnakbar(view: View,layoutId:Int): Snackbar {
        // create an instance of the snackbar
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
        // inflate the custom_snackbar_view created previously
        val customSnackView: View = layoutInflater.inflate(layoutId, null)
        // set the background of the default snackbar as transparent
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        // now change the layout of the snackbar
        val snackbarLayout = snackbar.view as SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        val trainId=customSnackView.findViewById(com.example.trainlivelocation.R.id.custom_snake_bar_txt_id_value) as TextView
        trainId.setText(binding.addPostTxtTrainId.text)
        snackbarLayout.addView(customSnackView, 0);
        return snackbar
    }

    companion object {
        var userModel: userResponseItem? = null
    }

    override fun onPauseFragment() {
        toast("onPauseFragment")
    }

    override fun onResumeFragment() {
        toast("onResumeFragment")
    }
}