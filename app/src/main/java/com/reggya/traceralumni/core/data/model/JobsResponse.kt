package com.reggya.traceralumni.core.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JobsResponse(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("job_position")
	val jobPosition: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("company")
	val company: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("link_job")
	val linkJob: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("job_level")
	val jobLevel: String? = null

) : Parcelable
