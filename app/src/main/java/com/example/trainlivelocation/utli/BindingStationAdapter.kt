package com.example.trainlivelocation.utli

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.domain.entity.DoctorResponseItem
import com.example.domain.entity.Location_Response
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

@BindingAdapter("setTrainNumber")
fun setAdapter(
    textview:TextView,
    trainNumber: Int
) {
    textview.setText("${trainNumber}")
}

@BindingAdapter("setMessageRCVAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: MessageCustomAdapter
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}


@BindingAdapter("setDocotrRCVAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: DoctorCustomAdapter
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}

@BindingAdapter("listener","doctor")
fun onNotifiyClicked(
    lotti: LottieAnimationView,
    listenr: DoctorListener,
    doctor:DoctorResponseItem
) {
    lotti.setOnClickListener{
        listenr.OnNotifyClickListener(doctor)
        lotti.playAnimation()
    }
}

@BindingAdapter("setLongitude")
fun setLongitude(
    view: TextView,
    location: Location_Response,
) {
    view.setText("Longitude : ${location.longitude.longitude}")
}

@BindingAdapter("setLatitude")
fun setLatitude(
    view: TextView,
    location: Location_Response,
) {
    view.setText("Latitude : ${location.latitude.latitude}")
}

