package com.example.domain.usecase

import Resource
import com.example.domain.entity.DoctorNotification
import com.example.domain.entity.NotificatonToken
import com.example.domain.repo.UserRepo

class SendDoctorNotificationUsingFCM(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        token: NotificatonToken,
        serverKey: String?,
        doctorNotification: DoctorNotification,
        result: (Resource<String>) -> Unit
    ) = userRepo.sendDoctorNotificationUsingFCM(token,serverKey,doctorNotification,result)
}