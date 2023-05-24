package com.example.trainlivelocation

import Resource
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.domain.entity.AddPostCommentNotificationData
import com.example.domain.entity.AddPostNotificationData
import com.example.domain.entity.NotificatonToken
import com.example.domain.repo.UserRepo
import com.example.domain.usecase.GetDataFromSharedPrefrences
import com.example.domain.usecase.SendUserNotificationTokenToFirebase
import com.example.trainlivelocation.ui.CriticalPost
import com.example.trainlivelocation.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG: String? = "MyFirebaseMessagingService"
    private val CHANNEL_ID = "notification_name"
    private val CHANNEL_NAME = "com.example.trainlivelocation"


    private fun tiramisuPermissionsCheck(): Boolean {
        // If we are above level 33, check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        Log.i(TAG, "message was deleted")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "TutorialNotificationsChannel"
        val channel = NotificationChannel(
            CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Our notification channel"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            var title: String? = remoteMessage.getData()["title"]
            var message: String? = remoteMessage.getData()["message"]

            when (title!!) {
                "doctors" -> {
                    //create doctors notification
                    var longitude = remoteMessage.getData()["longitude"]!!.toDouble()
                    var latitude = remoteMessage.getData()["latitude"]!!.toDouble()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("FRAGMENT_NAME", "DoctorLocationInMap")
                    intent.putExtra("doctorLocationLongitude", longitude)
                    intent.putExtra("doctorLocationLatitude", latitude)

                    val pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    getRemoteView(title!!, message!!, false)?.let {
                        generateNotification(
                            it,
                            pendingIntent
                        )
                    }
                }
                "stationAlarm" -> {


                }
                "stationHistory" -> {
                    //create station history notification


                }
                "NewPostComment" -> {
                    //create post comment notification
                    Log.i(TAG, "New Post Comment")
                    var adminId = remoteMessage.getData()["adminId"]!!.toInt()
                    var content = remoteMessage.getData()["content"]!!.toString()
                    var critical = remoteMessage.getData()["critical"]!!.toBoolean()
                    var date = remoteMessage.getData()["date"]!!.toString()
                    var id = remoteMessage.getData()["id"]!!.toInt()
                    var img = remoteMessage.getData()["img"]?.toString()
                    var imgId = remoteMessage.getData()["imgId"]!!.toString()
                    var trainNumber = remoteMessage.getData()["trainNumber"]!!.toInt()
                    var userId = remoteMessage.getData()["userId"]!!.toInt()
                    var userName = remoteMessage.getData()["userName"]!!.toString()
                    var userPhone = remoteMessage.getData()["userPhone"]!!.toString()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("FRAGMENT_NAME", "NewPostComment")
//                    intent.putExtra("trainID", trainID)
                    intent.putExtra(
                        "notificationModel",
                        AddPostCommentNotificationData(
                            title,
                            message!!,
                            adminId,
                            content,
                            critical,
                            date,
                            id,
                            img,
                            imgId,
                            trainNumber,
                            userId,
                            userName,
                            userPhone
                        )
                    )
//                    intent.putExtra("critical", criticalPost)
                    Log.i(TAG, "Post ID ${id::class.simpleName}")
                    val pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    getRemoteView(title, content, false)?.let {
                        generateNotification(
                            it,
                            pendingIntent
                        )
                    }

                }
                "postAdded" -> {
                    //create post  notification
                    Log.d(TAG, "post  notification ")
                    var trainID = remoteMessage.getData()["trainID"]!!.toInt()
                    var criticalPost = remoteMessage.getData()["critical"]!!.toBoolean()
                    var id = remoteMessage.getData()["postId"]!!.toInt()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("FRAGMENT_NAME", "AddPostFragment")
//                    intent.putExtra("trainID", trainID)
                    intent.putExtra(
                        "notificationModel",
                        AddPostNotificationData(title, message!!, trainID, criticalPost, id)
                    )
//                    intent.putExtra("critical", criticalPost)
                    Log.i(TAG, "Post ID ${id::class.simpleName}")
                    val pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    getRemoteView(title!!, message!!, false)?.let {
                        generateNotification(
                            it,
                            pendingIntent
                        )
                    }

                }
                "getInboxMessage" -> {
                    //create get inbox Message notification

                }
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private fun HandleDataPayload(data: MutableMap<String, String>) {
        val title = data["title"]
        val message = data["message"]
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        // Create a notification
        val notificationBuilder = NotificationCompat.Builder(this, "channelId")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.logo_icon_white)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "Channel name",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel description"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(0, notificationBuilder.build())

        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "onNewToken ---> ${token}")
        // Handle FCM token refresh here
//        CoroutineScope(Dispatchers.Main).launch {
//            getDataFromSharedPrefrences("userToken") {
//                when (it) {
//                    is Resource.Loading -> {
//                        Log.i(TAG, "Waiting for user data...")
//                    }
//                    is Resource.Success -> {
//                        if (it != null) {
//                            CoroutineScope(Dispatchers.Main).launch {
//                                sendUserNotificationTokenToFirebase(
//                                    NotificatonToken(
//                                        it.data.phone,
//                                        it.data.name,
//                                        token
//                                    )
//                                ) {
//                                    when (it) {
//                                        is Resource.Loading -> {
//                                            Log.i(TAG, "sending token...")
//                                        }
//                                        is Resource.Success -> {
//                                            Log.i(TAG, "${it.data}")
//                                        }
//                                        is Resource.Failure -> {
//                                            Log.i(TAG, "${it.error}")
//                                        }
//                                        else -> {
//
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    is Resource.Failure -> {
//                        Log.i(TAG, "${it.error}")
//                    }
//                    else -> {}
//                }
//            }
//        }

    }

    fun generateNotification(remoteViews: RemoteViews, pendingIntent: PendingIntent) {

        //channel id,name
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setSmallIcon(R.drawable.app_logo)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        builder = builder.setContent(remoteViews)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())

    }


    private fun getRemoteView(title: String, message: String, criticalPost: Boolean): RemoteViews? {
        var remoteView: RemoteViews? = null
        when (title) {
            "doctors" -> {
                //create doctors notification
                remoteView =
                    RemoteViews("com.example.trainlivelocation", R.layout.doctor_notification)
                remoteView.setTextViewText(R.id.doctor_notification_title, title)
                remoteView.setTextViewText(R.id.doctor_notification_content, message)
            }
            "postAdded" -> {
                //create post  notification
                remoteView =
                    RemoteViews("com.example.trainlivelocation", R.layout.add_post_notification)
                remoteView.setTextViewText(R.id.doctor_notification_title, title)
                remoteView.setTextViewText(R.id.doctor_notification_content, message)

            }
            "NewPostComment" -> {
                //create station history notification
                remoteView =
                    RemoteViews("com.example.trainlivelocation", R.layout.add_post_notification)
                remoteView.setTextViewText(R.id.doctor_notification_title, title)
                remoteView.setTextViewText(R.id.doctor_notification_content, message)

            }
            "addCommentToPost" -> {
                //create post comment notification

            }
            "getInboxMessage" -> {
                //create get inbox Message notification

            }
        }


        return remoteView
    }
}