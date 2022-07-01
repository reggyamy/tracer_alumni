package com.reggya.traceralumni.core.data.network

import com.reggya.traceralumni.core.data.model.*
import io.reactivex.rxjava3.core.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login2.php")
    fun getLogin(@Field("username") username : String,
                 @Field("password")password : String): Flowable<LoginResponse>

    @GET("checksurvey.php")
    fun isSurveyCompleted(@Query("student_id")student_id: String): Flowable<SurveyResponse>

    @FormUrlEncoded
    @POST("survey.php")
    fun getSurvey(@Field("student_id") id : String,
                  @Field("name") name: String,
                  @Field("major") major: String,
                  @Field("year_of_entry") yearsOfEntry: String,
                  @Field("graduation_year") graduationYear: String,
                  @Field("gpa") gpa: String,
                  @Field("job_status") jobStatus: String,
                  @Field("company") company:String,
                  @Field("company_address") companyAddress: String,
                  @Field("job_position") position: String,
                  @Field("year_of_work") yearOfWork: String,
                  @Field("salary") salary: String,
                  @Field("feedback") feedback: String
    ) : Flowable<List<SurveyResponse>>

    @GET("apivacancy.php")
    fun getJobs(): Flowable<List<JobsResponse>>

    @GET("searchjob.php")
    fun getSearchJobs(@Query("query")query: String): Flowable<List<JobsResponse>>

    @Multipart
    @POST("uploadimage.php")
    fun uploadImage(@Part("id") id: RequestBody,
                    @Part photo: MultipartBody.Part
    ): Flowable<UserResponse>

    @FormUrlEncoded
    @POST("updateprofile.php")
    fun updateProfile(@Field("id") id: String?,
                      @Field("alumni")alumni: String?,
                      @Field("job") job: String?,
                      @Field("address") address:String?,
                      @Field("about") about:String?,
                      @Field("password") password:String?
    ): Flowable<UserResponse>

    @GET("userbyid.php")
    fun getUserById(@Query("id") id: String):Flowable<UserResponse>

    @FormUrlEncoded
    @POST("postcomment.php")
    fun insertComments(@Field("post_id") post_id: String,
                       @Field("student_id") student_id:String,
                       @Field("name") name:String,
                       @Field("body") body:String
    ): Flowable<List<CommentsItem>>

    @FormUrlEncoded
    @POST("postlike.php")
    fun insertLike(@Field("post_id") post_id: String,
                   @Field("name") name:String
    ): Flowable<LikesItem>

    @GET("deletelike.php")
    fun deleteLike(@Query("post_id") post_id: String,
                   @Query("name") name:String
    ): Flowable<LikesItem>

    @Multipart
    @POST("insertpost.php")
    fun insertPost(
        @Part("user_id") user_id: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("description") description: RequestBody,
        @Part("link") link: RequestBody
    ): Flowable<List<PostResponse>>

    @GET("allpost.php")
    fun getAllPost():Flowable<List<PostResponse>>

    @GET("postsbyuserid.php")
    fun getPostsByUserId(@Query("user_id")user_id: String): Flowable<List<PostResponse>>
    
}