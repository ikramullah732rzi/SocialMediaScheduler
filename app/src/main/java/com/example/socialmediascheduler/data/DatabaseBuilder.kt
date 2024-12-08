package com.example.socialmediascheduler.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

object DatabaseBuilder {
    private var mediaDatabase: MediaDatabase? = null

    @OptIn(InternalCoroutinesApi::class)
    fun getDatabase(context: Context): MediaDatabase {
        if (mediaDatabase == null) {
             synchronized(this) {
                mediaDatabase = Room.databaseBuilder(
                    context,
                    MediaDatabase::class.java,
                    "media.db"
                ).setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                    .fallbackToDestructiveMigration().build()
            }
        }
        return mediaDatabase!!
    }

}