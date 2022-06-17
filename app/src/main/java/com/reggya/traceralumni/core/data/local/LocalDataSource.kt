package com.reggya.traceralumni.core.data.local

import com.reggya.traceralumni.core.data.local.entity.JobsEntity
import com.reggya.traceralumni.core.data.local.room.JobsDao
import io.reactivex.rxjava3.core.Flowable

class LocalDataSource private constructor(
    private val jobsDao: JobsDao
){

//    fun getAllJob(): Flowable<List<JobsEntity>> = jobsDao.getJobs()

    fun getJobBookmarks(): Flowable<List<JobsEntity>> = jobsDao.getJobBookmarks()

    fun insertJobs(jobs: JobsEntity) = jobsDao.insertJobs(jobs)

//    fun setJobBookmark(job: JobsEntity, state: Boolean) {
//        job.isBookmark = state
//        jobsDao.updateJobBookmark(job)
//    }

    fun deleteJobBookmark(job: JobsEntity)= jobsDao.deleteJobBookmark(job)

    fun getJobBookmark(id: String?): Flowable<JobsEntity> = jobsDao.getJobById(id)

    companion object{
        val instance: LocalDataSource? = null

        fun getInstance(jobsDao: JobsDao): LocalDataSource =
            instance ?: synchronized(this){
                instance ?: LocalDataSource(jobsDao)
            }


    }

}