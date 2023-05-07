package com.example.domain.usecase

import Resource
import com.example.domain.repo.UserRepo

class GetNotificationTokenFromFirebase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        userPhone: String?, result: (Resource<String?>) -> Unit
    ) = userRepo.getNotificationTokenFromFirebase(userPhone,result)
}