package com.reggya.traceralumni.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobsModel(

    val id: String?,
    var jobPosition: String?,
    var image: String?,
    var description: String?,
    var company: String?,
    var jobLevel: String?,
    var location: String?,
    var linkJob: String?,
    var isBookmark: Boolean

):Parcelable