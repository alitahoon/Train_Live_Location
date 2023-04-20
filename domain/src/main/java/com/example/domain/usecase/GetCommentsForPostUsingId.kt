package com.example.domain.usecase

import Resource
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.repo.UserRepo

class GetCommentsForPostUsingId(private val userRepo: UserRepo) {
    suspend operator fun invoke(
        postId: Int?,
        result: (Resource<ArrayList<PostCommentsResponseItem>>) -> Unit
    ) = userRepo.getCommentsForPostUsingId(postId, result)
}