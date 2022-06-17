package com.reggya.traceralumni.core.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SurveyResponse(

	@field:SerializedName("message")
	val message: Int,

	@field:SerializedName("job_status")
	val jobStatus: String? = null,

	@field:SerializedName("major")
	val major: String? = null,

	@field:SerializedName("year_of_work")
	val yearOfWork: String? = null,

	@field:SerializedName("graduation_year")
	val graduationYear: String? = null,

	@field:SerializedName("job_position")
	val jobPosition: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("student_id")
	val studentId: String? = null,

	@field:SerializedName("gpa")
	val gpa: String? = null,

	@field:SerializedName("company")
	val company: String? = null,

	@field:SerializedName("conpany_address")
	val conpanyAddress: String? = null,

	@field:SerializedName("salary")
	val salary: String? = null,

	@field:SerializedName("year_of_entry")
	val yearOfEntry: String? = null,

	@field:SerializedName("feedback")
	val feedback: String? = null

) : Parcelable
