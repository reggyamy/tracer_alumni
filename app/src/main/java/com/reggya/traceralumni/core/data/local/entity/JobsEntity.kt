package com.reggya.traceralumni.core.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Jobs")
data class JobsEntity(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name ="jobPosition")
    var jobPosition: String,

    @ColumnInfo(name ="image")
    var image: String,

    @ColumnInfo(name ="description")
    var description: String,

    @ColumnInfo(name ="company")
    var company: String,

    @ColumnInfo(name ="jobLevel")
    var jobLevel: String,

    @ColumnInfo(name ="location")
    var location: String,

    @ColumnInfo(name = "linkJob")
    var linkJob: String,

    @ColumnInfo(name = "isBookmark")
    var isBookmark: Boolean = false

)
