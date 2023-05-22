package com.example.domain.usecase

import Resource
import com.example.domain.entity.NotificationTokenResponseInTrain
import com.example.domain.repo.UserRepo

class ReportPost(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        postId: Int,
        userID: Int,
        reportReason: String,
        result: (Resource<NotificationTokenResponseInTrain>) -> Unit
    )=userRepo.reportPost(postId,userID,reportReason,result)
}