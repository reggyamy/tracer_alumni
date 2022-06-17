package com.reggya.traceralumni.core.utils

import com.reggya.traceralumni.core.data.local.entity.JobsEntity
import com.reggya.traceralumni.core.data.model.JobsResponse
import com.reggya.traceralumni.core.domain.model.JobsModel

object DataMapper {

    fun jobResponsesToEntities(response: List<JobsResponse>?): List<JobsEntity>{
        val jobs = ArrayList<JobsEntity>()
        if (response != null) {
            response.map { jobResponse ->
                val job = JobsEntity(
                    id = jobResponse.id.toString(),
                    jobPosition = jobResponse.jobPosition.toString(),
                    image = jobResponse.image.toString(),
                    description = jobResponse.description.toString(),
                    company = jobResponse.company.toString(),
                    jobLevel = jobResponse.jobLevel.toString(),
                    location = jobResponse.location.toString(),
                    linkJob = jobResponse.linkJob.toString(),
                    isBookmark = false
                )
                jobs.add(job)
            }
        }
        return jobs
    }

    fun jobEntitiestoDomain(entity: List<JobsEntity>): List<JobsModel>{
        val jobs = ArrayList<JobsModel>()
        entity.map { entity ->
            val job = JobsModel(
                id = entity.id,
                jobPosition = entity.jobPosition,
                image = entity.image,
                description = entity.description,
                company = entity.company,
                jobLevel = entity.jobLevel,
                location = entity.location,
                linkJob = entity.linkJob,
                isBookmark = entity.isBookmark
            )
            jobs.add(job)
        }
        return jobs
    }

    fun jobDomaintoEntity(input: JobsModel?): JobsEntity =
        JobsEntity(
            id = input?.id ?: "",
            jobPosition = input?.jobPosition ?: "",
            image = input?.image ?: "",
            description = input?.description ?: "",
            company = input?.company ?: "",
            jobLevel = input?.jobLevel ?: "",
            location = input?.location ?: "",
            linkJob = input?.linkJob ?: "",
            isBookmark = input?.isBookmark ?: false
        )


    fun jobResponsesToDomain(response: List<JobsResponse>?): List<JobsModel> {
        val jobs = ArrayList<JobsModel>()
        response?.map { jobResponse ->
            val job = JobsModel(
                id = jobResponse.id,
                jobPosition = jobResponse.jobPosition,
                image = jobResponse.image,
                description = jobResponse.description,
                company = jobResponse.company,
                jobLevel = jobResponse.jobLevel,
                location = jobResponse.location,
                linkJob = jobResponse.linkJob,
                isBookmark = false
            )
            jobs.add(job)
        }
      return jobs
    }
}