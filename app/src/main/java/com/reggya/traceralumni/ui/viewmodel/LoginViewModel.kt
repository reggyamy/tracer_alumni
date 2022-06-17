package com.reggya.traceralumni.ui.viewmodel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.reggya.traceralumni.core.domain.UseCase

class LoginViewModel(private val useCase: UseCase): ViewModel() {

    fun getUserLogin(username:String, password: String) =
        LiveDataReactiveStreams.fromPublisher(useCase.getUserLogin(username, password))


}
