package com.example.domain.usecase

import Resource
import com.example.domain.entity.DoctorNotification
import com.example.domain.repo.UserRepo

class GetDoctorNotificationFromFirebase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        userPhone: String,
        result: (Resource<ArrayList<DoctorNotification>>) -> Unit
    ) = userRepo.getDoctorNotificationFromFirebase(userPhone, result)
}