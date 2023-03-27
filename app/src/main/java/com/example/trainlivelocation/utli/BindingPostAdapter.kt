package com.example.trainlivelocation.utli

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.entity.Post
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.storage.FirebaseStorage

class BindingPostAdapter{
    companion object{
        @BindingAdapter("setAdapter")
        fun setAdapter(
            recyclerView: RecyclerView,
            adapter: PostCustomAdapter
        ) {
            adapter?.let {
                recyclerView.adapter = it
            }
        }

        @Suppress("UNCHECKED_CAST")
        @BindingAdapter("data")
        fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, data: T) {
            if (recyclerView.adapter is BindableAdapter<*>) {
                (recyclerView.adapter as BindableAdapter<T>).setData(data)
            }
        }

        @BindingAdapter("setImage")
        fun setImage(imageView: ImageView, urlString: String) {
            val storage: FirebaseStorage = FirebaseStorage.getInstance()
            // Create a storage reference from our app
            val storageRef = storage.reference
            var islandRef = storageRef.child("profileImages/" + urlString + ".jpg")
            val ONE_MEGABYTE: Long = 1024 * 1024
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                // Data for "images/island.jpg" is returned, use this as needed
                val image: Drawable = BitmapDrawable(BitmapFactory.decodeByteArray(it, 0, it.size))
                imageView.setImageDrawable(image)
            }.addOnFailureListener {
                Log.e("setUserProfileImage",it.message.toString())
            }}


        @BindingAdapter("setUserProfileImage")
        fun setUserProfileImage(imageView: ImageView, userphone: String) {
            val storage: FirebaseStorage = FirebaseStorage.getInstance()
            // Create a storage reference from our app
            val storageRef = storage.reference
            var islandRef = storageRef.child("profileImages/" + userphone + ".jpg")
            val ONE_MEGABYTE: Long = 1024 * 1024
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                // Data for "images/island.jpg" is returned, use this as needed
                val image: Drawable = BitmapDrawable(BitmapFactory.decodeByteArray(it, 0, it.size))
                imageView.setImageDrawable(image)
            }.addOnFailureListener {
                Log.e("setUserProfileImage",it.message.toString())
            }

            @BindingAdapter("manageState")
            fun manageState(progressBar: LinearProgressIndicator, state: LiveData<Boolean>) {
                progressBar.visibility = if (state.value!!) View.VISIBLE else View.GONE
            }

        }
}


}