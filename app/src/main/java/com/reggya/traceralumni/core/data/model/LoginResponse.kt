package com.reggya.traceralumni.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

    val message: String? = null,
    val name: String? = null,
    val username: String? = null

) : Parcelable
