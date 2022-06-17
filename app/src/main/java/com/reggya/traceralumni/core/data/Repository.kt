package com.reggya.traceralumni.core.data

import com.reggya.traceralumni.core.data.local.LocalDataSource
import com.reggya.traceralumni.core.data.local.entity.JobsEntity
import com.reggya.traceralumni.core.data.model.*
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.data.remote.RemoteDataSource
import com.reggya.traceralumni.core.domain.IRepository
import com.reggya.traceralumni.core.domain.model.JobsModel
import com.reggya.traceralumni.core.utils.AppExecutors
import com.reggya.traceralumni.core.utils.DataMapper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): IRepository {

    override fun getUserLogin(
        username: String,
        password: String
    ): Flowable<ApiResponse<LoginResponse>> {
        return remoteDataSource.getUserLogin(username, password)
    }

    override fun isSurveyCompleted(student_id: String): Flowable<ApiResponse<SurveyResponse>> =
        remoteDataSource.isSurveyCompleted(student_id)

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
        yearOfWork: String,
        position: String,
        salary: String,
        feedback: String
    ): Flowable<ApiResponse<SurveyResponse>> {
        return remoteDataSource.getSurveyUser(id,name,major, yearsOfEntry, graduationYear,gpa,jobStatus,
            company,companyAddress,yearOfWork,position,salary, feedback)
    }

    override fun getJobs(): Flowable<ApiResponse<List<JobsResponse>>> =
        remoteDataSource.getJobs()

    override fun getJobBookmarks(): Flowable<List<JobsModel>> =
        localDataSource.getJobBookmarks().map {
            DataMapper.jobEntitiestoDomain(it)
        }


    override fun getJobBookmark(id: String?): Flowable<JobsEntity> =
        localDataSource.getJobBookmark(id)

    override fun insertJobBookmark(job: JobsModel) {
        Flowable.fromCallable {
            val entity = DataMapper.jobDomaintoEntity(job)
            localDataSource.insertJobs(entity)
        }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ()
    }

    override fun deleteJobBookmark(job: JobsModel) {
        Flowable.fromCallable {
            val entity = DataMapper.jobDomaintoEntity(job)
            localDataSource.deleteJobBookmark(entity)
        }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ()
    }

    override fun getSearchJobs(query: String): Flowable<ApiResponse<List<JobsResponse>>> =
        remoteDataSource.getSearchJobs(query)

    override fun getUploadImage(id: String, photo: File): Flowable<ApiResponse<UserResponse>> {
        return remoteDataSource.getUploadImage(id, photo)
    }

    override fun getUpdateProfile(
        id: String?, alumni: String?, job: String?, address: String?, about: String?, password: String?
                                  ): Flowable<ApiResponse<UserResponse>> {
        return remoteDataSource.getUpdateProfile(id, alumni, job, address,  about, password
        )
    }

    override fun getUserById(id: String): Flowable<ApiResponse<UserResponse>> {
        return remoteDataSource.getUserById(id)
    }

    override fun insertPost(
        user_id: String,
        image: File?,
        description: String,
        link: String
    ): Flowable<ApiResponse<List<PostResponse>>> {
        return remoteDataSource.insertPost(user_id, image, description, link)
    }

    override fun insertComments(
        post_id: String,
        student_id: String,
        name: String,
        body: String
    ): Flowable<ApiResponse<List<CommentsItem>>> = remoteDataSource.insertComment(post_id, student_id, name, body)

    override fun insertLike(
        post_id: String,
        name: String
    ): Flowable<ApiResponse<LikesItem>> = remoteDataSource.insertLike(post_id, name)

    override fun deleteLike(
        post_id: String,
        name: String
    ): Flowable<ApiResponse<LikesItem>> = remoteDataSource.deleteLike(post_id, name)


    override fun getAllPost(): Flowable<ApiResponse<List<PostResponse>>> =
        remoteDataSource.getAllPost()

    override fun getPostsByUserId(user_id: String): Flowable<ApiResponse<List<PostResponse>>> =
        remoteDataSource.getPostsByUserId(user_id)

    companion object{
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource,
            appExecutors: AppExecutors
        ): Repository =
            instance ?: synchronized(this){
                instance ?: Repository(remoteDataSource, localDataSource, appExecutors)
            }
    }
}