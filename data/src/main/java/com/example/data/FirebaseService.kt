package com.example.data

import Resource
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.domain.entity.Message
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import java.util.concurrent.TimeUnit

class FirebaseService(
    private val auth: FirebaseAuth,
    private val storageRef: StorageReference,
    private val databaseRef: DatabaseReference
) {
    private val TAG: String? = "FirebaseService"

    companion object {
        var storedVerificationId: String? = null
        var storedtoken: PhoneAuthProvider.ForceResendingToken? = null
        var firebaseAuthActivity: AppCompatActivity? = null
        // The test phone number and code should be whitelisted in the console.
    }

    fun sendOtpToPhone(
        phoneNumber: String?,
        callback: (result: String?) -> Unit
    ) {
        Log.i(TAG, "${phoneNumber}")
        auth.setLanguageCode("ar")
        // Configure faking the auto-retrieval with the whitelisted numbers.
        auth.firebaseAuthSettings.forceRecaptchaFlowForTesting(true)
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(firebaseAuthActivity!!)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    //start sign in with credential
                    Log.d(TAG, "onVerificationCompleted:$credential")
                    signInWithPhoneAuthCredential(credential) {
                        Log.i(TAG, it!!)
                    }
                    callback("onVerificationCompleted")
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    //stop progress bar
                    callback("onVerificationFailed")
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        Log.e(TAG, "onVerificationFailed ${e.message}")
                    } else if (e is FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        Log.e(TAG, "FirebaseTooManyRequestsException ${e.message}")

                    }

                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, token)
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID
                    Log.d(TAG, "onCodeSent:$verificationId")
                    storedtoken = token
                    storedVerificationId = verificationId
                    callback("onCodeSent")
                }

            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendOtpCode(
        phoneNumber: String?, callback: (result: String?) -> Unit
    ) {

    }

    fun sendImageToFirebaseStorage(
        profileImagesUri: Uri,
        imagePath: String,
        result: (Resource<String>) -> Unit
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
        credential: PhoneAuthCredential,
        callback: (result: String?) -> Unit
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
                            TAG,
                            "FirebaseAuthInvalidCredentialsException:failure",
                            task.exception
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
        result: (Resource<String>) -> Unit
    ) {

        val key = databaseRef.push().key
        val map = HashMap<String, Any>()
        map["sender"] = senderPhone!!
        map["message"] = message!!
        Log.i(TAG, message)

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

        databaseRef.push()
            .setValue(Message(message, senderPhone, recieverPhone, senderPhone + recieverPhone)).addOnSuccessListener {
                Log.i(TAG, "sent successfully...")
                result.invoke(Resource.Success("Message Sent Successfully..."))
            }.addOnFailureListener {
                Log.i(TAG, "Failed :${it.message}")
                            result.invoke(Resource.Failure("${it.message}"))
            }
    }


    fun getChatMessagesFromFirebase(senderPhone: String?,recieverPhone: String?,result: (Resource<ArrayList<Message>>) -> Unit){
        var oldChatKey=arrayListOf<String>()
        //check if there is last chat
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val messageList= arrayListOf<Message>()
                for (snapshot in dataSnapshot.children) {
                    val message = snapshot.getValue(Message::class.java)
                    Log.i(TAG,"Title : ${message!!.title}")
                    if (message.title.equals(senderPhone+recieverPhone)||message.title.equals(recieverPhone+senderPhone)){
                        Log.e(TAG,"${snapshot.key}")
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

    fun getInboxRecieveChatFromFirebase(phone: String?,result: (Resource<ArrayList<Message>>) -> Unit){
        databaseRef.addValueEventListener(object : ValueEventListener{
            val messageList= arrayListOf<Message>()
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
    fun getInboxSentChatFromFirebase(phone: String?,result: (Resource<ArrayList<Message>>) -> Unit){
        databaseRef.addValueEventListener(object : ValueEventListener{
            val messageList= arrayListOf<Message>()
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


}
