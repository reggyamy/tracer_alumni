package com.reggya.traceralumni.core.data.remote

import android.annotation.SuppressLint
import android.util.Log
import com.reggya.traceralumni.core.data.model.*
import com.reggya.traceralumni.core.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RemoteDataSource private constructor(private val apiService: ApiService){

    @SuppressLint("LongLogTag")
    fun getUserLogin(username :String, password: String): Flowable<ApiResponse<LoginResponse>> {
        val resultData = PublishSubject.create<ApiResponse<LoginResponse>>()

        val client = apiService.getLogin(username, password)

        client
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({ response ->
                resultData.onNext(
                    ((if (response.username?.isNotEmpty() == true) ApiResponse.success(response)
                    else ApiResponse.empty()) as ApiResponse<LoginResponse>?)
                )
            },{ throwable ->
                resultData.onNext(ApiResponse.error(throwable.message.toString()))
                Log.e("RemoteDataSource getLogin", throwable.toString())
            })

        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun isSurveyCompleted(student_id: String): Flowable<ApiResponse<SurveyResponse>>{
        val result = PublishSubject.create<ApiResponse<SurveyResponse>>()

        val client = apiService.isSurveyCompleted(student_id)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    ApiResponse.success(response)
                )
            },{ throwable ->
                result.onNext(ApiResponse.error(throwable.message.toString()))
                Log.e("RemoteDataSource checkSurvey", throwable.toString())
            })
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun getSurveyUser(id : String, name: String, major: String, yearsOfEntry: String, graduationYear: String,
                      gpa: String, jobStatus: String, company:String, companyAddress: String,position: String, yearOfWork: String,
                      salary: String, feedback: String): Flowable<ApiResponse<List<SurveyResponse>>>{
        val resultData = PublishSubject.create<ApiResponse<List<SurveyResponse>>>()

        val client = apiService.getSurvey(id,name,major, yearsOfEntry, graduationYear,gpa,jobStatus,
                                        company,companyAddress,position,yearOfWork,salary, feedback)
            client
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe({ surveyResponse ->
                    resultData.onNext(
                        if (surveyResponse.isNotEmpty()) ApiResponse.success(surveyResponse)
                        else ApiResponse.empty()
                    )
                },{ throwable ->
                    resultData.onNext(ApiResponse.error(throwable.message.toString()))
                    Log.e("RemoteDataSource getSurveyUser", throwable.toString())
                })

        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun getJobs(): Flowable<ApiResponse<List<JobsResponse>>>{
        val result = PublishSubject.create<ApiResponse<List<JobsResponse>>>()

        val client = apiService.getJobs()
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({
                val job = it
                result.onNext(
                    (if (job.isNotEmpty()) ApiResponse.success(job)
                    else ApiResponse.empty()) as ApiResponse<List<JobsResponse>>?
                )
            },{error ->
                result.onNext(ApiResponse.error(error.message.toString()))
                Log.e("RemoteDataSource getJobs", error.toString())
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun getSearchJobs(query: String): Flowable<ApiResponse<List<JobsResponse>>>{
        val result = PublishSubject.create<ApiResponse<List<JobsResponse>>>()

        val client = apiService.getSearchJobs(query)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    if (response.isNotEmpty()) ApiResponse.success(response)
                    else ApiResponse.empty()
                )
            },{ error ->
                result.onNext(ApiResponse.error(error.message))
                Log.e("RemoteDataSource getSearchJobs", error.toString())
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun getUploadImage(id:String, photo: File): Flowable<ApiResponse<UserResponse>>{
        val result = PublishSubject.create<ApiResponse<UserResponse>>()

        val requestFile = photo
            .asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val image = MultipartBody.Part
            .createFormData("photo", photo.name, requestFile)

        val userId: RequestBody = id.toRequestBody("multipart/formdata".toMediaTypeOrNull())

        val client = apiService.uploadImage(userId, image)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({
                result.onNext(
                    if (it.username != null) ApiResponse.success(it)
                    else ApiResponse.empty()
                )
            },{
                result.onNext(ApiResponse.error(it.message))
                Log.e("RemoteDataSource update profile", it.toString())
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun getUpdateProfile(
        id: String?, alumni: String?, job:String?, address: String?, about:String?, password:String?
    ): Flowable<ApiResponse<UserResponse>>{
        val result = PublishSubject.create<ApiResponse<UserResponse>>()

        val client = apiService.updateProfile(id, alumni, job, address,  about, password)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({
                result.onNext(
                    if (it.username != null) ApiResponse.success(it)
                    else ApiResponse.empty()
                )
            },{
                result.onNext(ApiResponse.error(it.message))
                Log.e("RemoteDataSource update profile", it.toString())
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun getUserById(id: String): Flowable<ApiResponse<UserResponse>>{
        val result = PublishSubject.create<ApiResponse<UserResponse>>()

        val client = apiService.getUserById(id)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    if (response.username != null) ApiResponse.success(response)
                    else ApiResponse.empty()
                )
            },{ trowable ->
                result.onNext(ApiResponse.error(trowable.message))
                Log.e("RemoteDataSource getUserById", trowable.toString())
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun insertPost(
        user_id: String, image: File?, description: String, link: String
    ): Flowable<ApiResponse<List<PostResponse>>> {
        val result = PublishSubject.create<ApiResponse<List<PostResponse>>>()

        val requestFile = image?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val image_ = requestFile?.let {
            MultipartBody.Part
                .createFormData("image", image.name, it)
        }

        val userId_: RequestBody = user_id.toRequestBody("multipart/formdata".toMediaTypeOrNull())
        val description_ : RequestBody = description.toRequestBody("multipart/formdata".toMediaTypeOrNull())
        val link_ : RequestBody = link.toRequestBody("multipart/formdata".toMediaTypeOrNull())

        val client = apiService.insertPost(userId_ , image_, description_, link_)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    if (response.isNotEmpty()){
                        ApiResponse.success(response)
                    } else ApiResponse.empty()
                )
            },{ error ->
                result.onNext(ApiResponse.error(error.message))
                Log.e("RemoteDataSource insertPost", error.toString())
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun insertComment(
        post_id: String, student_id: String, name: String, body: String
    ): Flowable<ApiResponse<List<CommentsItem>>>{
        val result = PublishSubject.create<ApiResponse<List<CommentsItem>>>()

        val client = apiService.insertComments(post_id, student_id, name, body)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    if (response.isNotEmpty()) ApiResponse.success(response)
                    else ApiResponse.empty()
                )
            },{error ->
                result.onNext(ApiResponse.error(error.message.toString()))
                Log.e("RemoteDataSource insetComments", error.toString())
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun insertLike(
        post_id: String,name: String
    ): Flowable<ApiResponse<LikesItem>>{
        val result = PublishSubject.create<ApiResponse<LikesItem>>()

        val client = apiService.insertLike(post_id, name)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    if (response.name != null) ApiResponse.success(response)
                    else ApiResponse.empty()
                )
            },{error ->
                result.onNext(ApiResponse.error(error.message.toString()))
                Log.e("RemoteDataSource insetLike", error.toString())
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun deleteLike(
        post_id: String,name: String
    ): Flowable<ApiResponse<LikesItem>>{
        val result = PublishSubject.create<ApiResponse<LikesItem>>()

        val client = apiService.deleteLike(post_id, name)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    if (response.name != null) ApiResponse.success(response)
                    else ApiResponse.empty()
                )
            },{error ->
                result.onNext(ApiResponse.error(error.message.toString()))
                Log.e("RemoteDataSource deleteLike", error.toString())
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun getAllPost():Flowable<ApiResponse<List<PostResponse>>>{
        val result = PublishSubject.create<ApiResponse<List<PostResponse>>>()

        val client = apiService.getAllPost()
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe ({
                val dataArray = it
                result.onNext(
                    if (dataArray.isNotEmpty()){
                        ApiResponse.success(dataArray)
                    }else ApiResponse.empty()
                )
            },{
                result.onNext(ApiResponse.error(it.message.toString()))
                Log.e("RemoteDtaSource getAllPost", it.toString())
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("LongLogTag")
    fun getPostsByUserId(user_id: String): Flowable<ApiResponse<List<PostResponse>>>{
        val result = PublishSubject.create<ApiResponse<List<PostResponse>>>()

        val client = apiService.getPostsByUserId(user_id)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                result.onNext(
                    if (response.isNotEmpty()) ApiResponse.success(response)
                    else ApiResponse.empty()
                )
            },{error ->
                result.onNext(ApiResponse.error(error.message.toString()))
                Log.e("RemoteDataSource getPostById", error.toString())
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    companion object{
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource =
            instance ?: synchronized(this){
                instance ?: RemoteDataSource(apiService)
            }

    }
}