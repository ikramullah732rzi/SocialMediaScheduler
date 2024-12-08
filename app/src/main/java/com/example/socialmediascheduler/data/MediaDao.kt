package com.example.socialmediascheduler.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MediaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun insertData(mediaTable: MediaTable)

    @Update
  suspend  fun updateMedia(mediaTable: MediaTable)

    @Delete
   suspend fun deleteMedia(mediaTable: MediaTable)


    @Query("SELECT * FROM MediaTable ORDER BY id DESC")
    fun getMedia(): LiveData<List<MediaTable>>
}