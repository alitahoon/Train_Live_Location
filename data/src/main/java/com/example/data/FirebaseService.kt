package com.example.data

import android.app.Activity
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.storage.StorageReference
import java.util.concurrent.TimeUnit

class FirebaseService(
    private val auth: FirebaseAuth,
    private val activity: Activity,
    private val imageRef: StorageReference,
    private val phoneAuthOptions: PhoneAuthOptions,
    private val firebaseServiceCallbacks: FirebaseServiceCallbacks
) {
    private val TAG:String?="FirebaseService"

     fun sendOtpToPhone(phoneNumber: String?,callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        auth.setLanguageCode("ar")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

     fun resendOtpCode(phoneNumber: String?) {

    }

     fun sendProfileImageToFirebaseStorage(
        profileImagesUri: Uri,
        imageName: String,
    ) {
        profileImagesUri?.let {
            imageRef.child("profileImages/$imageName").putFile(it).addOnSuccessListener {
                firebaseServiceCallbacks.onImageSendSuccessfully()
            }.addOnFailureListener {
                Log.e(TAG,it.message.toString())
                firebaseServiceCallbacks.onImageSendFailure(it)
            }
        }
    }

     fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }

    }

     fun CreateAPhoneAuthCredentialWithCode(verificationId:String?,OtbCode:String?){
        val credential = PhoneAuthProvider.getCredential(verificationId!!, OtbCode!!)
    }



}