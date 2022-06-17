package com.reggya.traceralumni.ui.viewmodel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.reggya.traceralumni.core.data.model.CommentsItem
import com.reggya.traceralumni.core.data.model.LikesItem
import com.reggya.traceralumni.core.data.model.PostResponse
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.domain.UseCase
import io.reactivex.rxjava3.core.Flowable
import java.io.File

class PostViewModel(private val useCase: UseCase) : ViewModel() {

    fun insertPost(
        user_id: String, image: File?, description: String, link: String
    ) = LiveDataReactiveStreams.fromPublisher(useCase.insertPost(user_id, image, description, link))

    fun insertComments(
        post_id: String, student_id: String, name: String, body: String
    )= LiveDataReactiveStreams.fromPublisher(useCase.insertComments(post_id, student_id, name, body))

    fun insertLike(
        post_id: String, name: String
    )= LiveDataReactiveStreams.fromPublisher(useCase.insertLike(post_id, name))

    fun deleteLike(
        post_id: String, name: String
    )= LiveDataReactiveStreams.fromPublisher(useCase.deleteLike(post_id, name))

    fun getAllPost() = LiveDataReactiveStreams.fromPublisher(useCase.getAllPost())

    fun getPostsByUserId(user_id: String)= LiveDataReactiveStreams.fromPublisher(useCase.getPostsByUserId(user_id))

}

