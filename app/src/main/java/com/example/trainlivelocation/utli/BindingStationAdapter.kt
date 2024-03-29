package com.example.trainlivelocation.utli

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
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
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding
import com.example.trainlivelocation.ui.News
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*

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
@BindingAdapter("setNewsRCVAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: NewsCustomAdapter
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}

@BindingAdapter("animateItem")
fun animateItem(
    cardView: MaterialCardView,
    animation:Int
) {

    // Apply animation
    val animation = AnimationUtils.loadAnimation(cardView.context, R.anim.item_load_animation)
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
    textview: TextView,
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

@BindingAdapter("setDistance")
fun setDistance(
    textview: TextView,
    distance: Int
) {
    textview?.let {
        it.text = "Distance : ${distance} KM"
    }
}

@BindingAdapter("listener", "doctor")
fun onNotifiyClicked(
    lotti: LottieAnimationView,
    listenr: DoctorListener,
    doctor: DoctorResponseItem
) {
    lotti.setOnClickListener {
        listenr.OnNotifyClickListener(doctor)
        lotti.playAnimation()
    }
}

@BindingAdapter("setNumberPickerNumbers")
fun setNumberPickerNumbers(
    numberPicker: NumberPicker,
    maxValue:Int
) {

// Define the range of numbers
    val minValue = 1

// Set the range of numbers for the NumberPicker
    numberPicker.minValue = minValue
    numberPicker.maxValue = maxValue

// Create an array of strings to hold the numbers
    val numbers = Array(maxValue - minValue + 1) { (minValue + it).toString() }

// Set the displayed values for the NumberPicker
    numberPicker.displayedValues = numbers
}

@BindingAdapter("setLongitude")
fun setLongitude(
    view: TextView,
    location: Location_Response,
) {
    view.setText("Longitude : ${location.longitude}")
}

@BindingAdapter("setLatitude")
fun setLatitude(
    view: TextView,
    location: Location_Response,
) {
    view.setText("Latitude : ${location.latitude}")
}


@BindingAdapter("setPassengersRCVAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: PassengersCustomAdapter
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}

@BindingAdapter("setAlarmsAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: StationAlarmAdapterCustomAdapter
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}

@BindingAdapter("setAddressFromLocation")
fun setAddressFromLocation(
    view: TextView,
    location: Location_Response
) {
    Log.i("setAddressFromLocation", "${location.longitude},${location.latitude}")
    val geocoder: Geocoder
    val addresses: List<Address>?
    geocoder = Geocoder(view.context, Locale.getDefault())

    addresses = geocoder.getFromLocation(
        location.longitude,
        location.latitude,
        4
    ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

    val address: String =
        addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

    if (addresses[0] == null) {
        val city: String = addresses!![0].locality
        val state: String = addresses!![0].adminArea
        val country: String = addresses!![0].countryName
        val postalCode: String = addresses!![0].postalCode
        val knownName: String = addresses!![0].featureName // Only if available else return NULL
        view.setText(" ")
    } else {
        //get first name of state
        val stateArr = addresses!![0].adminArea.split(" ")
        view.setText("Address : ${addresses!![0].locality},${stateArr[0]},${addresses!![0].countryName}")
    }

}
