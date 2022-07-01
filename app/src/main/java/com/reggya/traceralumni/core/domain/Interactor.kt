package com.reggya.traceralumni.core.domain

import com.reggya.traceralumni.core.data.local.entity.JobsEntity
import com.reggya.traceralumni.core.data.model.*
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.domain.model.JobsModel
import io.reactivex.rxjava3.core.Flowable
import java.io.File

class Interactor (private val iRepository: IRepository): UseCase{

    override fun getUserLogin(
        username: String,
        password: String
    ): Flowable<ApiResponse<LoginResponse>> =
        iRepository.getUserLogin(username, password)

    override fun isSurveyCompleted(student_id: String): Flowable<ApiResponse<SurveyResponse>> =
        iRepository.isSurveyCompleted(student_id)

    override fun getSurveyUser(
        id: String,
        name: String,
        major: String,
        yearsOfEntry: String,
        graduationYear: String,
        gpa: String,
        jobStatus: String,
        company: String,
        companyAddress: String,
        position: String,
        yearOfWork: String,
        salary: String,
        feedback: String
    ): Flowable<ApiResponse<List<SurveyResponse>>> =
        iRepository.getSurveyUser(id,name,major, yearsOfEntry, graduationYear,gpa,jobStatus,
            company,companyAddress,position,yearOfWork,salary, feedback)

    override fun getJobs(): Flowable<ApiResponse<List<JobsResponse>>> =
        iRepository.getJobs()

    override fun getJobBookmarks(): Flowable<List<JobsModel>> =
        iRepository.getJobBookmarks()

    override fun getJobBookmark(id: String?): Flowable<JobsEntity> =
        iRepository.getJobBookmark(id)

    override fun insertJobBookmark(job: JobsModel) =
        iRepository.insertJobBookmark(job)

    override fun deleteJobBookmark(job: JobsModel) =
        iRepository.deleteJobBookmark(job)

    override fun getSearchJobs(query: String): Flowable<ApiResponse<List<JobsResponse>>> =
        iRepository.getSearchJobs(query)

    override fun getUploadImage(id: String, photo: File): Flowable<ApiResponse<UserResponse>> =
        iRepository.getUploadImage(id, photo)

    override fun getUpdateProfile(
        id: String?, alumni: String?, job: String?, address: String?, about: String?, password: String?
    ): Flowable<ApiResponse<UserResponse>> =
        iRepository.getUpdateProfile(id, alumni, job, address,  about, password)

    override fun getUserById(id: String): Flowable<ApiResponse<UserResponse>> =
        iRepository.getUserById(id)

    override fun insertPost(
        user_id: String,
        image: File?,
        description: String,
        link: String
    ): Flowable<ApiResponse<List<PostResponse>>> =
        iRepository.insertPost(user_id, image, description, link)

    override fun insertComments(
        post_id: String,
        student_id: String,
        name: String,
        body: String
    ): Flowable<ApiResponse<List<CommentsItem>>> = iRepository.insertComments(post_id, student_id, name, body)

    override fun insertLike(post_id: String, name: String): Flowable<ApiResponse<LikesItem>> =
        iRepository.insertLike(post_id, name)

    override fun deleteLike(post_id: String, name: String): Flowable<ApiResponse<LikesItem>> =
        iRepository.deleteLike(post_id, name)

    override fun getAllPost(): Flowable<ApiResponse<List<PostResponse>>> =
        iRepository.getAllPost()

    override fun getPostsByUserId(user_id: String): Flowable<ApiResponse<List<PostResponse>>> =
        iRepository.getPostsByUserId(user_id)


}