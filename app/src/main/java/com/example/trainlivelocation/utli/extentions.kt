package com.example.trainlivelocation.utli


import android.content.Context
import android.content.SharedPreferences
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R


//fun Context.setCurrentTopic(token: String,){
//    val sharedPreferences: SharedPreferences =
//        getSharedPreferences(TOPIC_PREFS, Context.MODE_PRIVATE)
//    sharedPreferences.edit().putString(SUBSCRIBED_TOPIC, token).apply()
//}
//fun Fragment.setCurrentTopic(context: Context,token: String){
//    val sharedPreferences: SharedPreferences =
//        context.getSharedPreferences(TOPIC_PREFS, Context.MODE_PRIVATE)
//    sharedPreferences.edit().putString(SUBSCRIBED_TOPIC, token).apply()
//}

fun Fragment.getuserModelFromSharedPreferences(context: Context): UserResponseItem{
    val userSharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserToken", Context.MODE_PRIVATE)
    return   UserResponseItem(
        userSharedPreferences.getString("userAddress","empty")!!,
        userSharedPreferences.getString("userBirthdate","empty")!!,
        userSharedPreferences.getString("userEmail","empty")!!,
        userSharedPreferences.getString("userGender","empty")!!,
        userSharedPreferences.getInt("userId",0),
        userSharedPreferences.getString("userJop","empty")!!,
        userSharedPreferences.getString("userName","empty")!!,
        userSharedPreferences.getString("userPassword","empty")!!,
        userSharedPreferences.getString("userPhone","empty")!!,
        userSharedPreferences.getString("userRole","empty")!!,
        userSharedPreferences.getString("tokenForNotifications","empty")!!
    )
}

fun Context.getuserModelFromSharedPreferences(): UserResponseItem{
    val userSharedPreferences: SharedPreferences =
        getSharedPreferences("UserToken", Context.MODE_PRIVATE)
    return   UserResponseItem(
        userSharedPreferences.getString("userAddress","empty")!!,
        userSharedPreferences.getString("userBirthdate","empty")!!,
        userSharedPreferences.getString("userEmail","empty")!!,
        userSharedPreferences.getString("userGender","empty")!!,
        userSharedPreferences.getInt("userId",0),
        userSharedPreferences.getString("userJop","empty")!!,
        userSharedPreferences.getString("userName","empty")!!,
        userSharedPreferences.getString("userPassword","empty")!!,
        userSharedPreferences.getString("userPhone","empty")!!,
        userSharedPreferences.getString("userRole","empty")!!,
        userSharedPreferences.getString("tokenForNotifications","empty")!!

    )
}

fun Context.insertUserCurrantTrainIntoSharedPrefrences(trainID:Int?){
    val trainSharedPreferences: SharedPreferences =
        getSharedPreferences("userCurrantStation", Context.MODE_PRIVATE)

    var editor=trainSharedPreferences.edit()
    editor.putInt("trainID",trainID!!)


}

fun Fragment.insertUserCurrantTrainIntoSharedPrefrences(context: Context,trainID:Int?){
    val trainSharedPreferences: SharedPreferences =
        context.getSharedPreferences("userCurrantStation", Context.MODE_PRIVATE)
    var editor=trainSharedPreferences.edit()
    editor.putInt("trainID",trainID!!)

}

fun Fragment.getUserCurrantTrainIntoSharedPrefrences(context: Context):Int?{
    val trainSharedPreferences: SharedPreferences =
        context.getSharedPreferences("userCurrantStation", Context.MODE_PRIVATE)
    return trainSharedPreferences.getInt("trainID",0)
}

fun Context.getUserCurrantTrainIntoSharedPrefrences():Int?{
    val trainSharedPreferences: SharedPreferences =
        getSharedPreferences("userCurrantStation", Context.MODE_PRIVATE)
    return trainSharedPreferences.getInt("trainID",0)
}


fun Context.showCustomToast(context: Context, message: String) {
    val inflater = LayoutInflater.from(context)
    val toastLayout = inflater.inflate(R.layout.custom_toast_layout, null)

    val toastTextView = toastLayout.findViewById<TextView>(R.id.custom_toast_text)
    toastTextView.text = message

    with(Toast(context)) {
        setGravity(Gravity.CENTER, 0, 0)
        duration = Toast.LENGTH_SHORT
        view = toastLayout
        show()
    }
}
fun Fragment.showCustomToast(context: Context, message: String) {
    val inflater = LayoutInflater.from(context)
    val toastLayout = inflater.inflate(R.layout.custom_toast_layout, null)

    val toastTextView = toastLayout.findViewById<TextView>(R.id.custom_toast_text)
    toastTextView.text = message

    with(Toast(context)) {
        setGravity(Gravity.BOTTOM, 10, 10)
        duration = Toast.LENGTH_SHORT
        view = toastLayout
        show()
    }
}

