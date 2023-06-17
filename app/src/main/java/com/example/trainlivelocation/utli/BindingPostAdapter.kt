package com.example.trainlivelocation.utli

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.entity.Post
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding
import com.example.trainlivelocation.ui.Add_post_fragment
import com.facebook.shimmer.Shimmer
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.storage.FirebaseStorage

@BindingAdapter("setAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: PostCustomAdapter
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}

//@Suppress("UNCHECKED_CAST")
//@BindingAdapter("data")
//fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, data: T) {
//    if (recyclerView.adapter is BindableAdapter<*>) {
//        (recyclerView.adapter as BindableAdapter<T>).setData(data)
//    }
//}

@BindingAdapter("userphone","imageId")
fun setImage(imageView: ImageView, userphone: String?,imageId:Int?) {
    val storage: FirebaseStorage = FirebaseStorage.getInstance()
    // Create a storage reference from our app
    val storageRef = storage.reference
    Log.i("setImage","${storageRef}postsImages/${userphone}/${imageId}")
    storageRef.child("postsImages/${userphone}/${imageId}").downloadUrl.addOnSuccessListener {
        Glide.with(imageView.context)
            .load(it)
            .placeholder(R.drawable.emptyicon)
            .into(imageView)

    }.addOnFailureListener{
        Log.i("setImageAdapterBinding","addOnFailureListener ${it.message}")
    }

}



@BindingAdapter("setUserProfileImage")
fun setUserProfileImage(imageView: ImageView, userphone: String?) {
    val storage: FirebaseStorage = FirebaseStorage.getInstance()
    // Create a storage reference from our app
    val storageRef = storage.reference
    Log.i("setUserProfileImage","${storageRef}ProfileImages/+20${userphone}")
    storageRef.child("/profileImages/+20${userphone}").downloadUrl.addOnSuccessListener {
        Glide.with(imageView.context)
            .load(it)
            .placeholder(R.drawable.post_profile_icon)
            .into(imageView)

    }.addOnFailureListener{
        Log.i("setUserProfileImage","addOnFailureListener ${it.message}")
    }
}


@BindingAdapter("setNewsImage")
fun setNewsImage(imageView: ImageView, imagURL: String?) {
    Glide.with(imageView.context)
        .load(imagURL)
        .placeholder(R.drawable.emptyicon)
        .into(imageView)
}