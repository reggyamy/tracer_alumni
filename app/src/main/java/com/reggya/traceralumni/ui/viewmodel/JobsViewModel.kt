package com.reggya.traceralumni.ui.viewmodel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.reggya.traceralumni.core.domain.UseCase
import com.reggya.traceralumni.core.domain.model.JobsModel

class JobsViewModel(private val useCase: UseCase): ViewModel() {

    //job jragment
    fun getSeachJobs(query: String) = LiveDataReactiveStreams.fromPublisher(useCase.getSearchJobs(query))

    fun getJobs() = LiveDataReactiveStreams.fromPublisher(useCase.getJobs())

    //detail fragment

//    fun setJobBookmark(jobsModel: JobsModel, state: Boolean) = useCase.setJobBoomark(jobsModel, state)

    fun insertJobBookmark(job: JobsModel) = useCase.insertJobBookmark(job)

    fun deleteJobBookmark(job: JobsModel) = useCase.deleteJobBookmark(job)

    fun checkBookmarked(id: String?) = LiveDataReactiveStreams.fromPublisher(useCase.getJobBookmark(id))

    //bookmark fragment
    fun getJobBookmarks() = LiveDataReactiveStreams.fromPublisher(useCase.getJobBookmarks())

}
