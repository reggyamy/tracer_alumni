package com.reggya.traceralumni.ui.viewmodel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.reggya.traceralumni.core.domain.UseCase
import java.io.File

class ProfileViewModel(private val useCase: UseCase): ViewModel() {

    fun getUpdateProfile(id:String?, alumni:String?, job:String?, address: String?, about:String?, password:String?) =
        LiveDataReactiveStreams.fromPublisher(useCase.getUpdateProfile(id, alumni, job, address,  about, password))

    fun getUserById(id: String)= LiveDataReactiveStreams.fromPublisher(useCase.getUserById(id))

    fun getUploadImage(id: String, photo: File) = LiveDataReactiveStreams.fromPublisher(useCase.getUploadImage(id, photo))
}