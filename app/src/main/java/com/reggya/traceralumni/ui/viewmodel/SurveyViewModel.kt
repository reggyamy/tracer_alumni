package com.reggya.traceralumni.ui.viewmodel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.reggya.traceralumni.core.domain.UseCase

class SurveyViewModel(private val useCase: UseCase): ViewModel() {

    fun isSurveyCompleted(student_id: String)= LiveDataReactiveStreams.fromPublisher(useCase.isSurveyCompleted(student_id))

    fun getSurveyUser(id: String,
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
    ) =
        LiveDataReactiveStreams.fromPublisher(useCase.getSurveyUser(id,name,major, yearsOfEntry, graduationYear,gpa,jobStatus,
            company,companyAddress,yearOfWork,position,salary, feedback))

}