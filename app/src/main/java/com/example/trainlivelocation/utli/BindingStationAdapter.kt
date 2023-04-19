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
@BindingAdapter("setStaionRCVAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: StationCustomAdapter
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}
@BindingAdapter("setTrainRCVAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: TrainCustomAdapter
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}

@BindingAdapter("setCommentRCVAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: CommentCustomAdapter
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}