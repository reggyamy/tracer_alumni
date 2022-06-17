package com.reggya.traceralumni.core.data.local.room

import androidx.room.*
import androidx.room.Dao
import com.reggya.traceralumni.core.data.local.entity.JobsEntity
import io.reactivex.rxjava3.core.Flowable

@Dao
interface JobsDao {

//    @Query("SELECT * FROM jobs")
//    fun getJobs(): Flowable<List<JobsEntity>>
//
//    @Query("SELECT * FROM jobs where isBookmark = 1")
//    fun getJobBookmarks():  Flowable<List<JobsEntity>>
//
//    @Insert( onConflict = OnConflictStrategy.REPLACE)
//    fun insertJobs(jobs: List<JobsEntity>): Completable
//
//    @Update
//    fun updateJobBookmark(job: JobsEntity)


    @Query("SELECT * FROM jobs")
    fun getJobBookmarks(): Flowable<List<JobsEntity>>

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    fun insertJobs(jobs: JobsEntity)

    @Query("SELECT * FROM jobs WHERE id= :id")
    fun getJobById(id: String?): Flowable<JobsEntity>

    @Delete
    fun deleteJobBookmark(job: JobsEntity)

}