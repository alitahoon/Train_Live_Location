package com.example.trainlivelocation.utli


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.domain.entity.Location_Response
import com.example.domain.entity.StationDistanceModel
import com.example.domain.entity.UserResponseItem
import com.example.trainlivelocation.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder


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

fun Fragment.setMapFlag(context: Context, flag: Boolean){
    val mapSharedPreferences: SharedPreferences = context.getSharedPreferences("map",Context.MODE_PRIVATE)

    val editor = mapSharedPreferences.edit()

    editor.putBoolean("mapFlag", flag)
    editor.commit()
}

fun Context.setMapFlag(flag: Boolean){
    val mapSharedPreferences: SharedPreferences = getSharedPreferences("map",Context.MODE_PRIVATE)

    val editor = mapSharedPreferences.edit()

    editor.putBoolean("mapFlag", flag)
    editor.commit()
}

fun Fragment.insertStationsHistoryNotificationSharedPreferences(context: Context, switchBtn: Boolean){
    val stationsHistoryNotificationSharedPreferences: SharedPreferences = context.getSharedPreferences("stationsHistoryNotification",Context.MODE_PRIVATE)

    val editor = stationsHistoryNotificationSharedPreferences.edit()

    editor.putBoolean("stationsHistory", switchBtn)
    editor.commit()
}

fun Context.insertStationsHistoryNotificationSharedPreferences(switchBtn: Boolean){
    val stationsHistoryNotificationSharedPreferences: SharedPreferences = getSharedPreferences("stationsHistoryNotification",Context.MODE_PRIVATE)

    val editor = stationsHistoryNotificationSharedPreferences.edit()

    editor.putBoolean("stationsHistory", switchBtn)
    editor.commit()
}

fun Fragment.getStationsHistoryNotificationSharedPreferences(context: Context,): Boolean{
    val stationsHistoryNotificationSharedPreferences: SharedPreferences =
        context.getSharedPreferences("stationsHistoryNotification", Context.MODE_PRIVATE)

    return stationsHistoryNotificationSharedPreferences.getBoolean("stationsHistory", true)
}

fun Context.getStationsHistoryNotificationSharedPreferences(): Boolean{
    val stationsHistoryNotificationSharedPreferences: SharedPreferences =
        getSharedPreferences("stationsHistoryNotification", Context.MODE_PRIVATE)

    return stationsHistoryNotificationSharedPreferences.getBoolean("stationsHistory", true)
}

fun Fragment.insertTrackTrainNotificationSharedPreferences(context: Context, switchBtn: Boolean){
    val trackTrainNotificationSharedPreferences: SharedPreferences = context.getSharedPreferences("trackTrainNotification",Context.MODE_PRIVATE)

    val editor = trackTrainNotificationSharedPreferences.edit()

    editor.putBoolean("trackTrain", switchBtn)
    editor.commit()
}
fun Context.insertTrackTrainNotificationSharedPreferences(switchBtn: Boolean){
    val trackTrainNotificationSharedPreferences: SharedPreferences = getSharedPreferences("trackTrainNotification",Context.MODE_PRIVATE)

    val editor = trackTrainNotificationSharedPreferences.edit()

    editor.putBoolean("trackTrain", switchBtn)
    editor.commit()
}

fun Fragment.getTrackTrainNotificationSharedPreferences(context: Context,): Boolean{
    val trackTrainNotificationSharedPreferences: SharedPreferences =
        context.getSharedPreferences("trackTrainNotification", Context.MODE_PRIVATE)

    return trackTrainNotificationSharedPreferences.getBoolean("trackTrain", true)
}

fun Context.getTrackTrainNotificationSharedPreferences(): Boolean{
    val trackTrainNotificationSharedPreferences: SharedPreferences =
        getSharedPreferences("trackTrainNotification", Context.MODE_PRIVATE)

    return trackTrainNotificationSharedPreferences.getBoolean("trackTrain", true)
}

fun Fragment.insertStationAlarmNotificationSharedPreferences(context: Context, switchBtn: Boolean){
    val stationAlarmNotificationSharedPreferences: SharedPreferences = context.getSharedPreferences("stationAlarmNotification",Context.MODE_PRIVATE)

    val editor = stationAlarmNotificationSharedPreferences.edit()

    editor.putBoolean("stationAlarm", switchBtn)
    editor.commit()
}
fun Context.insertStationAlarmNotificationSharedPreferences(switchBtn: Boolean){
    val stationAlarmNotificationSharedPreferences: SharedPreferences = getSharedPreferences("stationAlarmNotification",Context.MODE_PRIVATE)

    val editor = stationAlarmNotificationSharedPreferences.edit()

    editor.putBoolean("stationAlarm", switchBtn)
    editor.commit()
}

fun Fragment.getStationAlarmNotificationSharedPreferences(context: Context,): Boolean{
    val stationAlarmNotificationSharedPreferences: SharedPreferences =
        context.getSharedPreferences("stationAlarmNotification", Context.MODE_PRIVATE)

    return stationAlarmNotificationSharedPreferences.getBoolean("stationAlarm", true)
}

fun Context.getStationAlarmNotificationSharedPreferences(): Boolean{
    val stationAlarmNotificationSharedPreferences: SharedPreferences =
        getSharedPreferences("stationAlarmNotification", Context.MODE_PRIVATE)

    return stationAlarmNotificationSharedPreferences.getBoolean("stationAlarm", true)
}

fun Fragment.insertSharingLocationSharedPreferences(context: Context, switchBtn: Boolean){
    val sharingLocationSharedPreferences: SharedPreferences = context.getSharedPreferences("sharingLocationNotification",Context.MODE_PRIVATE)

    val editor = sharingLocationSharedPreferences.edit()

    editor.putBoolean("sharingLocation", switchBtn)
    editor.commit()
}
fun Context.insertSharingLocationSharedPreferences(switchBtn: Boolean){
    val sharingLocationSharedPreferences: SharedPreferences = getSharedPreferences("sharingLocationNotification",Context.MODE_PRIVATE)

    val editor = sharingLocationSharedPreferences.edit()

    editor.putBoolean("sharingLocation", switchBtn)
    editor.commit()
}

fun Fragment.getSharingLocationSharedPreferences(context: Context,): Boolean{
    val sharingLocationSharedPreferences: SharedPreferences =
        context.getSharedPreferences("sharingLocationNotification", Context.MODE_PRIVATE)

    return sharingLocationSharedPreferences.getBoolean("sharingLocation", true)
}

fun Context.getSharingLocationSharedPreferences(): Boolean{
    val sharingLocationSharedPreferences: SharedPreferences =
        getSharedPreferences("sharingLocationNotification", Context.MODE_PRIVATE)

    return sharingLocationSharedPreferences.getBoolean("sharingLocation", true)
}

fun Fragment.setLocationFromMap(context: Context, location: Location_Response){
    val mapSharedPreferences: SharedPreferences = context.getSharedPreferences("location",Context.MODE_PRIVATE)

    val editor = mapSharedPreferences.edit()
    val gson = Gson()
    val locationGson = gson.toJson(location)

    editor.putString("Location", locationGson)
    editor.commit()
}

fun Context.setLocationFromMap(location: Location_Response){
    val mapSharedPreferences: SharedPreferences = getSharedPreferences("location",Context.MODE_PRIVATE)

    val editor = mapSharedPreferences.edit()
    val gson = Gson()
    val locationGson = gson.toJson(location)

    editor.putString("Location", locationGson)
    editor.commit()
}

fun Fragment.isMapOpen(context: Context): Boolean{
    val mapSharedPreferences: SharedPreferences = context.getSharedPreferences("map",Context.MODE_PRIVATE)

    return mapSharedPreferences.getBoolean("mapFlag", false)
}

fun Context.isMapOpen(): Boolean{
    val mapSharedPreferences: SharedPreferences = getSharedPreferences("map",Context.MODE_PRIVATE)

    return mapSharedPreferences.getBoolean("mapFlag", false)
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

fun Fragment.getLocationSharedPrefrence(context: Context): Location_Response{
    val locationSharedPreferences: SharedPreferences =
        context.getSharedPreferences("location", Context.MODE_PRIVATE)
    val gson = Gson()
    val json = locationSharedPreferences.getString("Location", gson.toJson(Location_Response(30.062959005017905,31.2472764196547)))
    return gson.fromJson(json, Location_Response::class.java)
}

fun Context.getLocationSharedPrefrence(): Location_Response{
    val locationSharedPreferences: SharedPreferences =
        getSharedPreferences("location", Context.MODE_PRIVATE)
    val gson = Gson()
    val json = locationSharedPreferences.getString("Location", gson.toJson(Location_Response(30.062959005017905,31.2472764196547)))
    return gson.fromJson(json, Location_Response::class.java)
}

fun Fragment.setFirstTimeOpenSharedPreferences(context: Context, isFirstTime: Boolean){
    val firstTimeSharedPreferences: SharedPreferences =
        context.getSharedPreferences("setFirstTimeOpen", Context.MODE_PRIVATE)

    val editor = firstTimeSharedPreferences.edit()
    editor.putBoolean("isFirstTime", isFirstTime)
}

fun Context.setFirstTimeOpenSharedPreferences(isFirstTime: Boolean){
    val firstTimeSharedPreferences: SharedPreferences =
        getSharedPreferences("setFirstTimeOpen", Context.MODE_PRIVATE)

    val editor = firstTimeSharedPreferences.edit()
    editor.putBoolean("isFirstTime", isFirstTime)
}

fun Fragment.getFirstTimeOpenSharedPreferences(context: Context,): Boolean{
    val firstTimeSharedPreferences: SharedPreferences =
        context.getSharedPreferences("setFirstTimeOpen", Context.MODE_PRIVATE)

    return firstTimeSharedPreferences.getBoolean("isFirstTime", true)
}

fun Context.getFirstTimeOpenSharedPreferences(): Boolean{
    val firstTimeSharedPreferences: SharedPreferences =
        getSharedPreferences("setFirstTimeOpen", Context.MODE_PRIVATE)

    return firstTimeSharedPreferences.getBoolean("isFirstTime", true)
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

//fun Context.getStationHistoryAlarm(): StationDistanceModel?{
//    var stationModel:StationDistanceModel?=null
//    val trainSharedPreferences: SharedPreferences =
//        getSharedPreferences("stationHistory", Context.MODE_PRIVATE)
//    val gsonBuilder = GsonBuilder()
//    gsonBuilder.registerTypeAdapter(StationDistanceModel::class.java, StationDistanceDeserializer())
//    val gson = gsonBuilder.create()
//    val res=trainSharedPreferences.getString("stationData", null)
//        Log.i("getStationHistoryAlarm","$res")
//    stationModel = gson.fromJson(res, StationDistanceModel::class.java)
//
//    return stationModel
//}

fun Context.getStationHistoryAlarm(): StationDistanceModel? {
    var stationModel: StationDistanceModel? = null
    val trainSharedPreferences: SharedPreferences =
        getSharedPreferences("stationHistory", Context.MODE_PRIVATE)
    val gsonBuilder = GsonBuilder()
    gsonBuilder.registerTypeAdapter(StationDistanceModel::class.java, StationDistanceDeserializer())
    val gson = gsonBuilder.create()
    val res = trainSharedPreferences.getString("stationData", null)
    Log.i("getStationHistoryAlarm", "$res")
    stationModel = gson.fromJson(res, StationDistanceModel::class.java)

    return stationModel
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

