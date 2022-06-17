package com.reggya.traceralumni.core.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reggya.traceralumni.core.data.local.entity.JobsEntity

@Database(entities = [JobsEntity::class], version = 1, exportSchema = false )
abstract class AlumniDatabase: RoomDatabase() {

    abstract fun jobsDao(): JobsDao

    companion object{
        @Volatile
        private var INSTANCE: AlumniDatabase? = null

        fun getInstance(context: Context): AlumniDatabase =
            INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlumniDatabase::class.java,
                    "alumni.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

            INSTANCE = instance
            instance
            }
    }
}