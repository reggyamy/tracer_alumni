package com.reggya.traceralumni.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reggya.traceralumni.core.di.Injection
import com.reggya.traceralumni.core.domain.UseCase

class ViewModelFactory private constructor(
    private val useCase: UseCase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when{
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->{
                LoginViewModel(useCase) as T
            }

            modelClass.isAssignableFrom(SurveyViewModel::class.java) ->{
                SurveyViewModel(useCase) as T
            }

            modelClass.isAssignableFrom(JobsViewModel::class.java) ->{
                JobsViewModel(useCase) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) ->{
                ProfileViewModel(useCase) as T
            }

            modelClass.isAssignableFrom(PostViewModel::class.java) ->{
                PostViewModel(useCase) as T
            }

            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }


        companion object{
            private val instance: ViewModelFactory? = null

            fun getInstance(context: Context): ViewModelFactory =
                instance ?: synchronized(this){
                    instance ?: ViewModelFactory(Injection.provideUseCase(context))
                }
        }
}