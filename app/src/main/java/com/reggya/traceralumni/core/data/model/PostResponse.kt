package com.reggya.traceralumni.core.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostResponse(


    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("comments")
    val comments: List<CommentsItem?>? = null,

    @field:SerializedName("post_id")
    val postId: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("author")
    val author: Author? = null,

    @field:SerializedName("link")
    val link: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("likes")
    val likes: List<LikesItem?>? = null,

    var body: String? = null

) : Parcelable


@Parcelize
data class LikesItem(

    @field:SerializedName("name")
    val name: String? = null

) : Parcelable


@Parcelize
data class CommentsItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("student_id")
    val studentId: String? = null,

    @field:SerializedName("photo_profile")
    val photoProfile: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("body")
    val body: String? = null
) : Parcelable

@Parcelize
data class Author(

    @field:SerializedName("alumni")
    val alumni: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null
) : Parcelable