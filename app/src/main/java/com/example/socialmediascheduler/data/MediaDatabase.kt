package com.example.socialmediascheduler.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MediaTable::class), version = 4, exportSchema = false)
abstract class MediaDatabase : RoomDatabase() {
    abstract fun dao(): MediaDao
}