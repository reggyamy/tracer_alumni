package com.reggya.traceralumni.core.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResponse(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("password")
	val password: String?= null,

	@field:SerializedName("alumni")
	val alumni: String? = null,

	@field:SerializedName("about")
	val about: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("job")
	val job: String? = null
) : Parcelable
