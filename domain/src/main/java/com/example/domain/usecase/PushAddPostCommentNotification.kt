package com.example.domain.usecase

import Resource
import com.example.domain.entity.PushPostCommentNotification
import com.example.domain.repo.UserRepo

class PushAddPostCommentNotification(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        notification: PushPostCommentNotification,
        result: (Resource<String>) -> Unit
    ) = userRepo.pushAddPostCommentNotification(notification, result)
}