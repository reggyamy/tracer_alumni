package com.reggya.traceralumni.core.di

import android.content.Context
import com.reggya.traceralumni.core.data.Repository
import com.reggya.traceralumni.core.data.local.LocalDataSource
import com.reggya.traceralumni.core.data.local.room.AlumniDatabase
import com.reggya.traceralumni.core.data.network.ApiConfig
import com.reggya.traceralumni.core.data.remote.RemoteDataSource
import com.reggya.traceralumni.core.domain.IRepository
import com.reggya.traceralumni.core.domain.Interactor
import com.reggya.traceralumni.core.domain.UseCase
import com.reggya.traceralumni.core.utils.AppExecutors

object Injection {

    private fun provideRepository(context: Context): IRepository{
        val database = AlumniDatabase.getInstance(context)

        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.provideApiService())
        val localDataSource = LocalDataSource.getInstance(database.jobsDao())
        val appExecutors = AppExecutors()

        return Repository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }

    fun provideUseCase(context: Context): UseCase{
        val repository = provideRepository(context)
        return Interactor(repository)
    }
}