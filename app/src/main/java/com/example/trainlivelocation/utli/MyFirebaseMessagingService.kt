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
import androidx.core.content.ContextCompat
import com.example.domain.entity.NotificatonToken
import com.example.domain.usecase.GetDataFromSharedPrefrences
import com.example.domain.usecase.SendUserNotificationTokenToFirebase
import com.example.trainlivelocation.R
import com.example.trainlivelocation.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG:String?="MyFirebaseMessagingService"
    private val CHANNEL_ID="notification_name"
    private val CHANNEL_NAME="com.example.trainlivelocation"

    @Inject
    lateinit var sendUserNotificationTokenToFirebase:SendUserNotificationTokenToFirebase

    @Inject
    lateinit var getDataFromSharedPrefrences:GetDataFromSharedPrefrences
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)
/*----------------------- Show a notification based on the message -------------------- */
        if (tiramisuPermissionsCheck()) {
            // Create a notification
            val intent = Intent(this, MainActivity::class.java)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationID = 101

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(notificationManager)
            }
            // Clears other activities until our MainActivity opens up
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // So when we click the notification we are going to open the main activity
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            // Create the actual notification
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(remoteMessage.data["title"])
                .setContentText(remoteMessage.data["message"])
                .setColor(Color.GREEN)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            // Showing the notification
            notificationManager.notify(notificationID, notification)
        }
    }
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
    private fun handleNow(remoteMessage:RemoteMessage?) {
        CoroutineScope(Dispatchers.IO).launch {
            // Return the result to the main thread
            val result=getRemoteView(remoteMessage!!.notification!!.title!!,remoteMessage.notification!!.body!!)
            withContext(Dispatchers.Main) {
                // Send the notification
                Log.i(TAG,"New notification...")
                if (remoteMessage!=null){
                    generateDoctorNotification(result!!)
                }
            }
        }
    }

//    private fun scheduleJob() {
//        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
//
//        // Create a new job to handle the FCM message
//        val job = dispatcher.newJobBuilder()
//            .setService(MyJobService::class.java)
//            .setTag("my-fcm-job")
//            .setRecurring(false)
//            .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
//            .setTrigger(Trigger.executionWindow(0, 60))
//            .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
//            .setReplaceCurrent(false)
//            .build()
//
//        // Schedule the job
//        dispatcher.mustSchedule(job)
//    }
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//        val from: String? = remoteMessage.getFrom()
//        val data = remoteMessage.getData()
//
//        Log.d(TAG, "From: ${remoteMessage.from}")
//        if (remoteMessage.notification != null) {
//            Log.d(TAG, "Notification Message Body: ${remoteMessage.notification?.body}")
//
//            val notificationBuilder = NotificationCompat.Builder(this, "default")
//                .setContentTitle(remoteMessage.notification?.title)
//                .setContentText(remoteMessage.notification?.body)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setSmallIcon(R.drawable.logo_icon_white)
//            val notificationManager = NotificationManagerCompat.from(this)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val channel = NotificationChannel(
//                    "default",
//                    "Channel name",
//                    NotificationManager.IMPORTANCE_HIGH
//                ).apply {
//                    description = "Channel description"
//                }
//                val notificationManager = getSystemService(NotificationManager::class.java)
//                notificationManager.createNotificationChannel(channel)
//                if (ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.POST_NOTIFICATIONS
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return
//                }
//                notificationManager.notify(0, notificationBuilder.build())
//            }
//
        // Create a coroutine scope
//        CoroutineScope(Dispatchers.IO).launch {
//            // Return the result to the main thread
//            val result=getRemoteView(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
//            withContext(Dispatchers.Main) {
//                // Send the notification
//                Log.i(TAG,"New notification...")
//                if (remoteMessage!=null){
//                    generateDoctorNotification(result!!)
//                }
//            }
//        }
//
//
//        }
//    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG,"onNewToken")
        // Handle FCM token refresh here
        CoroutineScope(Dispatchers.Main).launch{
            getDataFromSharedPrefrences("userToken"){
                when(it){
                    is Resource.Loading->{
                        Log.i(TAG,"Waiting for user data...")
                    }
                    is Resource.Success->{
                        if (it!=null){
                            CoroutineScope(Dispatchers.Main).launch {
                                sendUserNotificationTokenToFirebase(NotificatonToken(it.data.phone,it.data.name,token)){
                                    when(it){
                                        is Resource.Loading->{
                                            Log.i(TAG,"sending token...")
                                        }
                                        is Resource.Success->{
                                            Log.i(TAG,"${it.data}")
                                        }
                                        is Resource.Failure->{
                                            Log.i(TAG,"${it.error}")
                                        }
                                        else -> {

                                        }
                                    }
                                }
                            }
                        }
                    }
                    is Resource.Failure->{
                        Log.i(TAG,"${it.error}")
                    }
                    else -> {}
                }
            }
        }

    }

    fun generateDoctorNotification(remoteViews: RemoteViews){
        val intent= Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        //channel id,name
        var builder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext,CHANNEL_ID)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setSmallIcon(R.drawable.app_logo)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        builder=builder.setContent(remoteViews)
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel=  NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())

    }

    private fun getRemoteView(title: String, message: String): RemoteViews? {
        val remoteView= RemoteViews("com.example.trainlivelocation", R.layout.doctor_notification)
        remoteView.setTextViewText(R.id.doctor_notification_title,title)
        remoteView.setTextViewText(R.id.doctor_notification_content,message)

        return remoteView
    }
}