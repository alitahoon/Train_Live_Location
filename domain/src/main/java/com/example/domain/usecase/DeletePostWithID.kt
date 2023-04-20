package com.example.domain.usecase

import Resource
import com.example.domain.entity.PostCommentsResponseItem
import com.example.domain.entity.PostModelResponse
import com.example.domain.repo.UserRepo

class DeletePostWithID (private val userRepo: UserRepo) {
    suspend operator fun invoke(postID:Int?,result: (Resource<PostModelResponse>)->Unit)=userRepo.deletePostWithID(postID,result)
}