package com.example.domain.usecase

import Resource
import android.net.Uri
import com.example.domain.repo.UserRepo

class GetImageFromFirebaseStorage(private val userRepo: UserRepo) {
    suspend operator fun invoke(imageRef:String?,result: (Resource<Uri>) -> Unit)=userRepo.getImageFromFirebaseStorage(imageRef,result)
}