package com.example.socialmediascheduler.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MediaTable")
data class MediaTable(
    @PrimaryKey(autoGenerate = true)
    var id : Int=0,
    @ColumnInfo(name = "descriptionText")
    var descriptionText :String,
    @ColumnInfo(name = "image")
    var imagepath: String
)