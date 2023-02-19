package com.example.domain.usecase

import android.net.Uri
import com.example.domain.repo.UserRepo
import com.google.firebase.storage.StorageReference

class SendProfileImageToFirebaseStorage(private val userRepo: UserRepo) {
    suspend operator fun invoke(profileImageUri: Uri, imageName: String,imageReference: StorageReference) =
        userRepo.sendProfileImageToFirebaseStorage(profileImageUri,imageName,imageReference)
}