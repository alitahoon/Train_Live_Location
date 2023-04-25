package com.example.trainlivelocation.utli

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.trainlivelocation.R
import com.google.android.material.snackbar.Snackbar


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}


fun displaySnackbarSuccess(context: Context, view: View?, text: String?,lottiSrc:Int?,snakeBackroundColor:Int?) {
    // create an instance of the snackbar
    val snackbar = Snackbar.make(view!!, "", Snackbar.LENGTH_SHORT)
    // inflate the custom_snackbar_view created previously
    val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    val customSnackView: View =
        inflater.inflate(R.layout.custom_snake_bar_layout, null)
    // set the background of the default snackbar as transparent
    snackbar.view.setBackgroundColor(Color.TRANSPARENT)
    // now change the layout of the snackbar
    val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
    snackbarLayout.setPadding(0, 0, 0, 0)
    val snakebarTxt =
        customSnackView.findViewById(R.id.custom_type_snake_bar_txt) as TextView
    val snakebarLotti =
        customSnackView.findViewById(R.id.custom_type_snake_bar_lotti) as LottieAnimationView
    val snakebarRootLayout =
        customSnackView.findViewById(R.id.custom_type_snake_bar_root_layout) as LinearLayout

    snakebarRootLayout.setBackgroundColor(ContextCompat.getColor(context,snakeBackroundColor!!))
    snakebarTxt.setText(text)
    snakebarLotti.setAnimation(lottiSrc!!)

    snackbarLayout.addView(customSnackView, 0);
    snackbar.show()
}




