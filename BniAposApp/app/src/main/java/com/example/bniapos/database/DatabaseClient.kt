package com.example.bniapos.database

import android.content.Context
import androidx.room.Room


class DatabaseClient private constructor(private val mCtx: Context) {

    val appDatabase: AppDatabase

    companion object {
        private var mInstance: DatabaseClient? = null

        @Synchronized
        fun getInstance(mCtx: Context): DatabaseClient? {
            if (mInstance == null) {
                mInstance = DatabaseClient(mCtx)
            }
            return mInstance
        }
    }

    init {
        val builder =
            Room.databaseBuilder(mCtx.applicationContext, AppDatabase::class.java, "BniApos")
        builder.build()
        appDatabase = builder.allowMainThreadQueries().build()
    }

}