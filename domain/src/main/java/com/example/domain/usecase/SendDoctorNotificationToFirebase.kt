package com.example.domain.usecase

import Resource
import com.example.domain.entity.DoctorNotification
import com.example.domain.repo.UserRepo

class SendDoctorNotificationToFirebase(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        doctoreNotification: DoctorNotification,
        result: (Resource<String>) -> Unit
    ) = userRepo.sendDoctorNotificationToFirebase(doctoreNotification,result)
}