package com.example.data

import Resource
import android.app.Activity
import android.net.Credentials
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.util.Log
import com.example.domain.entity.userResponseItem
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.storage.StorageReference
import java.util.concurrent.TimeUnit

class FirebaseService(
    private val auth: FirebaseAuth,
    private val storageRef: StorageReference,
){
    private val TAG: String? = "FirebaseService"
    companion object{
        var storedVerificationId:String?=null
        var storedtoken: PhoneAuthProvider.ForceResendingToken?=null
        var firebaseAuthActivity:AppCompatActivity?=null
        // The test phone number and code should be whitelisted in the console.
    }
    fun sendOtpToPhone(
        phoneNumber: String?,
        callback: (result: String?) -> Unit
    ){
        Log.i(TAG,"${phoneNumber}")
        auth.setLanguageCode("ar")
        // Configure faking the auto-retrieval with the whitelisted numbers.
        auth.firebaseAuthSettings.forceRecaptchaFlowForTesting(true)
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(firebaseAuthActivity!!)
            .setCallbacks(object :OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    //start sign in with credential
                    Log.d(TAG, "onVerificationCompleted:$credential")
                    signInWithPhoneAuthCredential(credential){
                        Log.i(TAG,it!!)
                    }
                    callback("onVerificationCompleted")
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    //stop progress bar
                    callback("onVerificationFailed")
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        Log.e(TAG,"onVerificationFailed ${e.message}")
                    } else if (e is FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        Log.e(TAG,"FirebaseTooManyRequestsException ${e.message}")

                    }

                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, token)
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID
                    Log.d(TAG, "onCodeSent:$verificationId")
                    storedtoken=token
                    storedVerificationId=verificationId
                    callback("onCodeSent")
                }

            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendOtpCode(phoneNumber: String?,callback: (result: String?) -> Unit
    ) {

    }

    fun sendImageToFirebaseStorage(
        profileImagesUri: Uri,
        imagePath: String,
        result: (Resource<String>)->Unit
    ) {
        profileImagesUri?.let {
            storageRef.child(imagePath).putFile(it).addOnSuccessListener {

                result(Resource.Success(  "Image upload to ${imagePath} successfully  ${it.metadata.toString()}"))
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
                        Log.w(TAG, "FirebaseAuthInvalidCredentialsException:failure", task.exception)
                    }
                    // Update UI
                }
            }

    }

    fun CreateAPhoneAuthCredentialWithCode(
         OtbCode: String?, callback: (result: PhoneAuthCredential?) -> Unit
    ) {
        var vrCode= storedVerificationId
        Log.i(TAG,"${OtbCode} ,${vrCode}")
        val credential = PhoneAuthProvider.getCredential(vrCode!!, OtbCode!!)
        callback(credential)
    }

    fun setActivity(activity: AppCompatActivity){
        Log.i(TAG,"setActivity")
        firebaseAuthActivity=activity
    }




}
