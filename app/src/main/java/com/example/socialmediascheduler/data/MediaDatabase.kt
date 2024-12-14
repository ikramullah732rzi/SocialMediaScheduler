package com.example.socialmediascheduler.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(MediaTable::class), version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MediaDatabase : RoomDatabase() {
    abstract fun dao(): MediaDao
}