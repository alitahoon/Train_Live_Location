package com.example.domain.usecase

import Resource
import android.net.Uri
import com.example.domain.repo.UserRepo
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.storage.StorageReference

class SendImageToFirebaseStorage(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        profileImageUri: Uri,
        imagePath: String,
        result: (Resource<String>)->Unit
    ) =
        userRepo.sendImageToFirebaseStorage(profileImageUri, imagePath, result)
}