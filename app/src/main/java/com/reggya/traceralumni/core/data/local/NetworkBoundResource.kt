//package com.reggya.traceralumni.core.data.local
//
//import com.reggya.traceralumni.core.data.Resource
//import com.reggya.traceralumni.core.data.remote.ApiResponse
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
//import io.reactivex.rxjava3.core.BackpressureStrategy
//import io.reactivex.rxjava3.core.Flowable
//import io.reactivex.rxjava3.disposables.CompositeDisposable
//import io.reactivex.rxjava3.schedulers.Schedulers
//import io.reactivex.rxjava3.subjects.PublishSubject
//
//abstract class NetworkBoundResource<ResultType : Any, RequestType> {
//
//    private val result = PublishSubject.create<Resource<ResultType>>()
//    private val compositeDisposable = CompositeDisposable()
//
//    init {
//        val dbSource = loadFromDatabase()
//        val db = dbSource.subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .take(1)
//            .subscribe {
//                dbSource.unsubscribeOn(Schedulers.io())
//                if (shouldFetch(it)) {
//                    fetchFromNetwork()
//                }
//                else{
//                    result.onNext(Resource.Success(it))
//                }
//            }
//
//        compositeDisposable.add(db)
//
//    }
//
//
//    private fun fetchFromNetwork() {
//        val apiResponse = createCall()
//
//        result.onNext(Resource.Loading(null))
//        val response = apiResponse.subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .take(1)
//            .doOnComplete{compositeDisposable.dispose()}
//            .subscribe { response ->
//                when (response) {
//                    ApiResponse.success(response.data) -> {
//                        saveCallResult(response.data)
//                        val dbSource = loadFromDatabase()
//                        dbSource.subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .take(1)
//                            .subscribe {
//                                dbSource.subscribeOn(Schedulers.io())
//                                result.onNext(Resource.Success(it))
//                            }
//                    }
//                    ApiResponse.loading() -> {
//                        val dbSource = loadFromDatabase()
//                        dbSource.subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .take(1)
//                            .subscribe {
//                                dbSource.subscribeOn(Schedulers.io())
//                                result.onNext(Resource.Success(it))
//                            }
//                    }
//                    ApiResponse.error(response.message) -> {
//                        onFetchFailed()
//                        result.onNext(Resource.Error(response.message.toString(), null))
//                    }
//                }
//            }
//
//        compositeDisposable.add(response)
//
//    }
//
//    protected open fun onFetchFailed(){}
//
//    protected abstract fun saveCallResult(data: RequestType?)
//
//    protected abstract fun createCall(): Flowable<ApiResponse<RequestType>>
//
//    protected abstract fun shouldFetch(data: ResultType?): Boolean
//
//    protected abstract fun loadFromDatabase(): Flowable<ResultType>
//
//    fun asFlowable(): Flowable<Resource<ResultType>> =
//        result.toFlowable(BackpressureStrategy.BUFFER)
//}