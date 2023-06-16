package com.example.domain.usecase

import Resource
import com.example.domain.entity.NotificationTokenResponseInTrain
import com.example.domain.entity.ReportPostRequset
import com.example.domain.entity.ReportPostResponse
import com.example.domain.repo.UserRepo

class ReportPost(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        postId: Int,
        userID: Int,
        reportReason: String,
        result: (Resource<String>) -> Unit
    )=userRepo.reportPost(postId,userID,reportReason,result)
}