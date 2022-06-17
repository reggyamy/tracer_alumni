package com.reggya.traceralumni.core.data.remote


data class ApiResponse<out T>(val type: ApiResponseType, val data: T?, val message: String?){
    companion object{
        fun empty() = ApiResponse(ApiResponseType.EMPTY, null, null)
        fun error(message: String?) = ApiResponse(ApiResponseType.ERROR, null, message)
        fun <T> success(value: T) = ApiResponse(ApiResponseType.SUCCESS, value, null)
    }
}

enum class ApiResponseType {
    ERROR, SUCCESS, EMPTY, INTERNET_LOST
}