package com.example.data

import Resource
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.domain.entity.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit


class FirebaseService(
    private val auth: FirebaseAuth,
    private val storageRef: StorageReference,
    private val databaseRef: DatabaseReference,
    private val firebaseMessaging: FirebaseMessaging
) {
    private val TAG: String? = "FirebaseService"
    lateinit var  mCallbacks: OnVerificationStateChangedCallbacks
    companion object {
        var storedVerificationId: String? = null
        var storedtoken: PhoneAuthProvider.ForceResendingToken? = null
        var firebaseAuthActivity: AppCompatActivity? = null
        // The test phone number and code should be whitelisted in the console.
    }

    fun sendOtpToPhone(
        phoneNumber: String?, result: (result: Resource<String>) -> Unit

    ) {
        Log.i(TAG, "${phoneNumber}")
        auth.setLanguageCode("ar")
        mCallbacks=object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //start sign in with credential
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential) {
                    Log.i(TAG, it!!)
                }
                result.invoke(Resource.Success("onVerificationCompleted"))
            }

            override fun onVerificationFailed(e: FirebaseException) {
                //stop progress bar
                result.invoke(Resource.Success("onVerificationFailed"))
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e(TAG, "onVerificationFailed ${e.message}")
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.e(TAG, "FirebaseTooManyRequestsException ${e.message}")
                    result.invoke(Resource.Failure("onVerificationCompleted ${e.message}"))

                }

            }

            override fun onCodeSent(
                verificationId: String, token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID
                Log.d(TAG, "onCodeSent:$verificationId")
                storedtoken = token
                storedVerificationId = verificationId
                result.invoke(Resource.Success("onCodeSent"))
            }

        }

        // Configure faking the auto-retrieval with the whitelisted numbers.
        val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS).setActivity(firebaseAuthActivity!!)
            .setCallbacks(mCallbacks).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendOtpCode(
        phoneNumber: String?, result: (result: Resource<String>) -> Unit
    ) {
        Log.i(TAG, "${phoneNumber}")
        auth.setLanguageCode("ar")
        // Configure faking the auto-retrieval with the whitelisted numbers.
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber!!) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(firebaseAuthActivity!!) // Activity (for callback binding)
            .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(storedtoken!!) // ForceResendingToken from callbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    fun sendImageToFirebaseStorage(
        profileImagesUri: Uri, imagePath: String, result: (Resource<String>) -> Unit
    ) {
        profileImagesUri?.let {
            storageRef.child(imagePath).putFile(it).addOnSuccessListener {

                result(Resource.Success("Image upload to ${imagePath} successfully  ${it.metadata.toString()}"))
            }.addOnFailureListener {
                result(Resource.Failure("Image upload to ${imagePath} faild  ${it.message}"))
            }
        }
    }

    fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential, callback: (result: String?) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(firebaseAuthActivity!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    callback("success")
                } else {
                    callback("failure")
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.w(
                            TAG, "FirebaseAuthInvalidCredentialsException:failure", task.exception
                        )
                    }
                    // Update UI
                }
            }

    }

    fun CreateAPhoneAuthCredentialWithCode(
        OtbCode: String?, callback: (result: PhoneAuthCredential?) -> Unit
    ) {
        var vrCode = storedVerificationId
        Log.i(TAG, "${OtbCode} ,${vrCode}")
        val credential = PhoneAuthProvider.getCredential(vrCode!!, OtbCode!!)
        callback(credential)
    }

    fun setActivity(activity: AppCompatActivity) {
        Log.i(TAG, "setActivity")
        firebaseAuthActivity = activity
    }

    fun getImageFromFireBaseStorage(imageRef: String?, result: (Resource<Uri>) -> Unit) {
        Log.e(TAG, "from getImageFromFireBaseStorage -> ${imageRef}")
        storageRef.child(imageRef!!).downloadUrl.addOnSuccessListener {
            result.invoke(Resource.Success(it))
        }.addOnFailureListener {
            result.invoke(Resource.Failure(it.message))
        }
    }

    fun sendMessageToChat(
        message: String?,
        senderPhone: String?,
        recieverPhone: String?,
        senderUsername: String?,
        recieverUsername: String?,
        result: (Resource<String>) -> Unit
    ) {

//        val key = databaseRef.push().key
//        val map = HashMap<String, Any>()
//        map["sender"] = senderPhone!!
//        map["message"] = message!!
//        Log.i(TAG, message)
//        databaseRef.child(key!!).child("title").setValue(senderPhone+recieverPhone).addOnSuccessListener {
//            databaseRef.child(key).child("messages").push().setValue(map).addOnSuccessListener {
//                result.invoke(Resource.Success("Message Sent Successfully..."))
//            }.addOnFailureListener {
//                result.invoke(Resource.Failure("${it.message}"))
//            }
//
//        }.addOnFailureListener {
//            result.invoke(Resource.Failure("${it.message}"))
//        }

        databaseRef.child("chats").push().setValue(
            Message(
                message,
                senderUsername,
                recieverUsername,
                senderPhone,
                recieverPhone,
                senderPhone + recieverPhone
            )
        ).addOnSuccessListener {
            Log.i(TAG, "sent successfully...")
            result.invoke(Resource.Success("Message Sent Successfully..."))
        }.addOnFailureListener {
            Log.i(TAG, "Failed :${it.message}")
            result.invoke(Resource.Failure("${it.message}"))
        }
    }


    fun getChatMessagesFromFirebase(
        senderPhone: String?, recieverPhone: String?, result: (Resource<ArrayList<Message>>) -> Unit
    ) {
        var oldChatKey = arrayListOf<String>()
        //check if there is last chat
        databaseRef.child("chats").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val messageList = arrayListOf<Message>()
                for (snapshot in dataSnapshot.children) {
                    val message = snapshot.getValue(Message::class.java)
                    Log.i(TAG, "Title : ${message!!.title}")
                    if (message.title.equals(senderPhone + recieverPhone) || message.title.equals(
                            recieverPhone + senderPhone
                        )
                    ) {
                        Log.e(TAG, "${snapshot.key}")
                        messageList.add(message)
                    }
                }
                result.invoke(Resource.Success(messageList))
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                result.invoke(Resource.Failure("${error.message}"))
            }
        })
    }

    fun getInboxRecieveChatFromFirebase(
        phone: String?, result: (Resource<ArrayList<Message>>) -> Unit
    ) {
        databaseRef.child("chats").addValueEventListener(object : ValueEventListener {
            val messageList = arrayListOf<Message>()
            override fun onDataChange(snapshot: DataSnapshot) {
                val message = snapshot.getValue(Message::class.java)
                for (Snapshot in snapshot.children) {
                    val message = Snapshot.getValue(Message::class.java)
                    Log.i(TAG, "Title : ${message!!.title}")
                    if (message.reciever.equals(phone)) {
                        Log.e(TAG, "${snapshot.key}")
                        messageList.add(message)
                    }
                }
                result.invoke(Resource.Success(messageList))
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                result.invoke(Resource.Failure("${error.message}"))
            }

        })


    }

    fun getInboxSentChatFromFirebase(
        phone: String?, result: (Resource<ArrayList<Message>>) -> Unit
    ) {
        databaseRef.child("chats").addValueEventListener(object : ValueEventListener {
            val messageList = arrayListOf<Message>()
            override fun onDataChange(snapshot: DataSnapshot) {
                val message = snapshot.getValue(Message::class.java)
                for (Snapshot in snapshot.children) {
                    val message = Snapshot.getValue(Message::class.java)
                    Log.i(TAG, "Title : ${message!!.title}")
                    if (message.sender.equals(phone)) {
                        Log.e(TAG, "${snapshot.key}")
                        messageList.add(message)
                    }
                }
                result.invoke(Resource.Success(messageList))
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                result.invoke(Resource.Failure("${error.message}"))
            }

        })


    }


    fun sendDoctorNotification(
        doctorNotification: DoctorNotification, result: (Resource<String>) -> Unit
    ) {
        databaseRef.child("Notifications").child("DoctorsNotification").push()
            .setValue(doctorNotification).addOnSuccessListener {
                result.invoke(Resource.Success("Notification sent Successfully"))
            }.addOnFailureListener {
                result.invoke(Resource.Success("Notification sent Failed ---> ${it.message}"))
            }
    }

    fun getNotificationFromFirebase(
        userPhone: String, result: (Resource<ArrayList<DoctorNotification>>) -> Unit
    ) {
        databaseRef.child("Notifications").child("DoctorsNotification").orderByChild("doctorPhone")
            .equalTo(userPhone).addValueEventListener(object : ValueEventListener {
                val doctorNotificationList = arrayListOf<DoctorNotification>()
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (doctorSnapshot in snapshot.children) {
                        doctorNotificationList.add(doctorSnapshot.getValue(DoctorNotification::class.java)!!)
                    }
                    result.invoke(Resource.Success(doctorNotificationList))
                }

                override fun onCancelled(error: DatabaseError) {
                    result.invoke(Resource.Failure("Failed on getting doctor Notification ----> ${error.message}"))
                }

            })
    }


    fun sendUserNotificatonToken(
        token: NotificatonToken?, result: (Resource<String?>) -> Unit
    ) {
        firebaseMessaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful && token!!.token.equals(" ")) {
                // Get new FCM registration token
                val userToken = task.result
                token.token = userToken
                databaseRef.child("UsersNotificationToken").child(token.userPhone!!).setValue(token)
                    .addOnCompleteListener(OnCompleteListener {
                        if (it.isSuccessful) {
                            result.invoke(Resource.Success(userToken))
                        } else {
                            result.invoke(Resource.Failure("Failed Saving token---> ${it.exception}"))
                        }
                    })
            } else if (!token!!.token.equals(" ")) {
                // here user want to just refresh token
                databaseRef.child("UsersNotificationToken").child(token.userPhone!!)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val notificatonToken = snapshot.getValue(NotificatonToken::class.java)
                            val key = snapshot.key
                            databaseRef.child("UsersNotificationToken").child(key!!).setValue(token)
                                .addOnCompleteListener(OnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        result.invoke(Resource.Success(notificatonToken!!.token))
                                    } else {
                                        result.invoke(Resource.Failure("Failed Saving token---> ${task.exception}"))
                                    }
                                })
                            databaseRef.removeEventListener(this)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            result.invoke(Resource.Failure("Failed Saving token---> ${error.message}"))
                        }

                    })
            } else {
                result.invoke(Resource.Failure("Fetching FCM registration token failed ${task.exception}"))
            }

        })
    }

    fun getNotificationToken(
        userPhone: String?, result: (Resource<String>) -> Unit
    ) {
        databaseRef.child("UsersNotificationToken").child(userPhone!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val notificatonToken = snapshot.getValue(NotificatonToken::class.java)
                    val token = notificatonToken?.token
                    Log.i(TAG, "UsersNotificationToken -->$notificatonToken")
                    if (token != null) {
                        result.invoke(Resource.Success(token))
                    } else {
                        result.invoke(Resource.Failure("Failed Getting token"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    result.invoke(Resource.Failure("Failed Getting token---> ${error.message}"))
                }
            })
    }

    fun sendDoctorNotificationUsingFCM(
        token: NotificatonToken,
        serverKey: String,
        doctorNotification: DoctorNotification,
        result: (Resource<String>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i(TAG, "${token.token} \n ${serverKey} \n ${doctorNotification} \n")
                // Set the target registration token
                val targetToken = token.token!!

                // Create a notification message
                val notification = HashMap<String, String>()
                notification["title"] = "Need Doctor"
                notification["body"] = doctorNotification.content

                // Create the message payload
                val message = HashMap<String, Any>()
                message["notification"] = notification
                message["to"] = targetToken

                // Send the message to FCM server
                val url = URL("https://fcm.googleapis.com/fcm/send")
                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                conn.setRequestProperty("Authorization", "key=${serverKey}")
                conn.setRequestProperty("Content-Type", "application/json")
                conn.requestMethod = "POST"
                conn.doOutput = true
                val writer = OutputStreamWriter(conn.outputStream)
                writer.write(Gson().toJson(message))
                writer.flush()

                // Handle the response
                val responseCode: Int = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "responseCode --> ${responseCode}")
                    result.invoke(Resource.Success("Notification Sent To Doctor  ${responseCode}"))
                } else {
                    // Notification failed to send
                    Log.e(TAG, "responseCode --> ${responseCode}")
                    result.invoke(Resource.Failure("Failed Sending Notification"))
                }
            } catch (e: Exception) {
                // Handle any exceptions that occur during the network operation
                result.invoke(Resource.Failure("Failed Sending Notification: ${e.message}"))
            }
        }
    }


    fun subscribeToNewTopic(
        topicInput: String, result: (Resource<String>) -> Unit
    ) {
        FirebaseMessaging.getInstance().subscribeToTopic(topicInput)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(Resource.Success("successfully subscribed to the topic--->${topicInput}"))
                } else {
                    println("failed to subscribe to the topic")
                }
            }
            .addOnFailureListener {
                result.invoke(Resource.Failure("failed to subscribe to the topic : ${it.message}"))
            }
    }

    fun createUserNotificationToken(result: (Resource<String>) -> Unit) {
        firebaseMessaging.token.addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                result.invoke(Resource.Success(it.result))
            } else {
                result.invoke(Resource.Failure("${it.exception}"))
            }
        })
    }

    fun sendNewNotificationToTopic(
        notification: PushNotification, result: (Resource<String>) -> Unit
    ) {
        var apiManager: ApiManager = ApiManager()
        CoroutineScope(Dispatchers.Main).launch {
            apiManager.postNotification(notification, result)
        }
    }

    fun sendNewNotificationToAddedPostTopic(
        notification: PushPostNotification, result: (Resource<String>) -> Unit
    ) {
        var apiManager: ApiManager = ApiManager()
        CoroutineScope(Dispatchers.Main).launch {
            apiManager.postAddedNotification(notification, result)
        }
    }

    fun sendNewNotificationToAddedPostCommentTopic(
        notification: PushPostCommentNotification, result: (Resource<String>) -> Unit
    ) {
        var apiManager: ApiManager = ApiManager()
        CoroutineScope(Dispatchers.Main).launch {
            apiManager.postCommentAddedNotification(notification, result)
        }
    }
    fun sendNewNotificationToMessageTopic(
        notification: PushMessageNotification, result: (Resource<String>) -> Unit
    ) {
        var apiManager: ApiManager = ApiManager()
        CoroutineScope(Dispatchers.Main).launch {
            apiManager.MessageNotification(notification, result)
        }
    }


}
