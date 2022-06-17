package com.reggya.traceralumni.core.domain

import com.reggya.traceralumni.core.data.local.entity.JobsEntity
import com.reggya.traceralumni.core.data.model.*
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.domain.model.JobsModel
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.Field
import java.io.File

interface IRepository {

    fun getUserLogin(username: String, password: String): Flowable<ApiResponse<LoginResponse>>

    fun isSurveyCompleted(student_id: String): Flowable<ApiResponse<SurveyResponse>>

    fun getSurveyUser(id: String,
                      name: String,
                      major: String,
                      yearsOfEntry: String,
                      graduationYear: String,
                      gpa: String,
                      jobStatus: String,
                      company: String,
                      companyAddress: String,
                      yearOfWork: String,
                      position: String,
                      salary: String,
                      feedback: String
    ): Flowable<ApiResponse<SurveyResponse>>

    fun getJobs(): Flowable<ApiResponse<List<JobsResponse>>>

    fun getJobBookmarks(): Flowable<List<JobsModel>>

    fun getJobBookmark(id: String?): Flowable<JobsEntity>

    fun insertJobBookmark(job: JobsModel)

    fun deleteJobBookmark(job: JobsModel)

    fun getSearchJobs(query: String): Flowable<ApiResponse<List<JobsResponse>>>

    fun getUploadImage(id: String, photo:File): Flowable<ApiResponse<UserResponse>>

    fun getUpdateProfile(
        id: String?, alumni: String?, job: String?, address: String?, about: String?, password: String?
    )
    : Flowable<ApiResponse<UserResponse>>

    fun getUserById(id: String): Flowable<ApiResponse<UserResponse>>

    fun insertPost(
        user_id: String, image: File?, description: String, link: String
    ):Flowable<ApiResponse<List<PostResponse>>>

    fun insertComments(
        post_id: String, student_id: String, name: String, body: String
    ): Flowable<ApiResponse<List<CommentsItem>>>

    fun insertLike(
        post_id: String, name: String
    ): Flowable<ApiResponse<LikesItem>>

    fun deleteLike(
        post_id: String, name: String
    ): Flowable<ApiResponse<LikesItem>>

    fun getAllPost(): Flowable<ApiResponse<List<PostResponse>>>

    fun getPostsByUserId(user_id: String): Flowable<ApiResponse<List<PostResponse>>>
}
