package com.example.bniapos.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bniapos.database.doas.ControlDao
import com.example.bniapos.database.entities.ControlTable

@Database(entities = [ControlTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun controlDao(): ControlDao?
}