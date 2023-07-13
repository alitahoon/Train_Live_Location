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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.*
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostFragmentBinding
import com.example.trainlivelocation.utli.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout


class Add_post_fragment : Fragment(), FragmentLifecycle, Train_Dialog_Listener {
    private val TAG: String? = "Add_post_fragment"
    private lateinit var binding: FragmentAddPostFragmentBinding
    private val addPostFragmentViewmodel: Add_post_fragment_ViewModel by activityViewModels()
    private val REQUSET_CODE_IMAGE: Int? = 108
    val postContent = MutableLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = addPostFragmentViewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        userModel = getuserModelFromSharedPreferences(requireContext())

        setObserver()
        return binding.root
    }

    private fun setObserver() {
        binding.addPostBtnSubmit.setOnClickListener{
            if (trainID==null){
                showCustomToast(requireContext(),"please select train id")
            }else{
                addPostFragmentViewmodel.addPost(
                    Post(
                        " ",
                        trainID!!,
                        true,
                        "postsImages/${userModel!!.phone}/${userModel!!.id}",
                        userModel!!.id,
                        userModel!!.phone,
                        userModel!!.name,
                        "${userModel!!.id}"
                    )
                )
            }



            addPostFragmentViewmodel.sendPostImageToFirebase!!.observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is Resource.Loading -> {
                            binding.addPostProgressBar.setVisibility(View.VISIBLE)
                        }
                        is Resource.Success -> {
                            Log.i(TAG, "${it.data}")
                            //get token
                            val token =
                                getuserModelFromSharedPreferences(requireContext()).tokenForNotifications
                            Log.i(TAG, "${token}")
                            //get User Token
                            addPostFragmentViewmodel.getUsersTokenInTrainById(
                                trainID!!
                            )

                            addPostFragmentViewmodel.usersTokenInTrain.observe(
                                viewLifecycleOwner,
                                Observer {
                                    when (it) {
                                        is Resource.Loading -> {
                                            Log.i(TAG, "Waiting for getting users Tokens..")
                                        }
                                        is Resource.Success -> {
                                            Log.i(TAG, "${it.data}")
                                            sendNotificationToUsers(it.data)
                                        }
                                        is Resource.Failure -> {
                                            Log.i(TAG, "${it.error}")
                                        }
                                        else -> {

                                        }
                                    }
                                })


                        }
                        is Resource.Failure -> {
                            binding.addPostProgressBar.setVisibility(View.INVISIBLE)
//                                getSnakbar(
//                                    binding.addPostBtnSubmit,
//                                    R.layout.custom_snake_bar_add_post_failed_layout
//                                ).show()
                            showCustomToast(requireContext(), "Post Published successfully...")
                        }
                        else -> {
                            Log.i(TAG, "Failed else brunch")
                        }
                    }
                })


            addPostFragmentViewmodel.post?.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Loading -> {
                        binding.addPostProgressBar.setVisibility(View.VISIBLE)
                    }
                    is Resource.Success -> {
                        Log.e(TAG, "${it}")
                        postID = it.data.id
                        addPostFragmentViewmodel?.sendPostImageToFirebase(
                            postImageUri!!,
                            "postsImages/${userModel!!.phone}/${it.data.id}"
                        )

                    }
                    is Resource.Failure -> {
                        Log.e(TAG, "${it.error}")
                        binding.addPostProgressBar.setVisibility(View.INVISIBLE)
                        getSnakbar(
                            binding.addPostBtnSubmit,
                            com.example.trainlivelocation.R.layout.custom_snake_bar_add_post_failed_layout
                        ).show()
                    }
                    else -> {
                        binding.addPostProgressBar.setVisibility(View.INVISIBLE)
                        Log.i(TAG, "Failed else brunch")
                    }
                }
            })

        }


        binding.addPostTxtTrainId.setOnClickListener {
            var dialog = ChooseTrainDialogFragment(this)
            var childFragmentManager = getChildFragmentManager()
            dialog.show(childFragmentManager, "ChooseTrainDialogFragment")
        }
        binding.addPostImageViewPostImage.setOnClickListener {
            //get image uri
            getImageUri()
            Log.i(TAG, "Add_post_fragment")
        }


        addPostFragmentViewmodel?.btnChooseImageClicked?.observe(viewLifecycleOwner, Observer {
            if (it == true) {

            }
        })

        addPostFragmentViewmodel.btnChooseTrainClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {

            }
        })
        addPostFragmentViewmodel.AddedPostNotification!!.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Resource.Success -> {
                        Log.i(TAG, "${it.data}")
                        clearFields()
                    }
                    is Resource.Failure -> {
                        Log.e(TAG, "${it.error}")
                    }
                    is Resource.Loading -> {
                        Log.e(
                            TAG,
                            "Waiting for notifying train users for new post..."
                        )
                    }
                    else -> {

                    }
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

    fun getSnakbar(view: View, layoutId: Int): Snackbar {
        // create an instance of the snackbar
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
        // inflate the custom_snackbar_view created previously
        val customSnackView: View = layoutInflater.inflate(layoutId, null)
        // set the background of the default snackbar as transparent
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        // now change the layout of the snackbar
        val snackbarLayout = snackbar.view as SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        val trainId =
            customSnackView.findViewById(com.example.trainlivelocation.R.id.custom_snake_bar_txt_id_value) as TextView
        trainId.setText(binding.addPostTxtTrainId.text)
        snackbarLayout.addView(customSnackView, 0);
        return snackbar
    }

    companion object {
        var userModel: UserResponseItem? = null
        private var postID: Int? = null
        private var postImageUri: Uri? = null
        private var trainID:Int?=null
//        private var postContent:String?=null
//        private var trainID:Int?=null


    }

    override fun onPauseFragment() {
        Log.i(TAG, "onPauseFragment")
    }

    override fun onResumeFragment() {
        Log.i(TAG, "onResumeFragment")
    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        Log.i(TAG, "onTrainSelected")
        binding.addPostTxtTrainId.setText("${trainId}")
        trainID=trainId!!
    }



    fun sendNotificationToUsers(notificationTokenList: ArrayList<NotificationTokenResponseInTrain>) {
        Log.i(TAG,"Users will notified for post \n $notificationTokenList")
        for (token in notificationTokenList) {
            //push notification
                addPostFragmentViewmodel.sendAddedPostNotificationPost(
                    PushPostNotification(
                        (AddPostNotificationData
                            (
                            "postAdded",
                            "New Post Added To Train with id :${binding.addPostTxtTrainId.text.toString()}",
                            trainID!!,
                            true,
                            postID!!
                        )), token.userToken
                    )
                )

        }
        binding.addPostProgressBar.setVisibility(View.INVISIBLE)
        getSnakbar(
            binding.addPostBtnSubmit,
            R.layout.custom_snake_bar_add_post_success_layout
        ).show()
    }


    fun clearFields() {
        binding.addPostTxtPostContent.setText("")
        trainID=1
        binding.addPostTxtTrainId.setText("")
        binding.addPostImageViewPostImage.setImageDrawable(requireActivity().getDrawable(R.drawable.add_photo_icon))
    }
}