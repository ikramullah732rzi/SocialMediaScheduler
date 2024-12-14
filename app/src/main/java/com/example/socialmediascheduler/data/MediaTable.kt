package com.example.socialmediascheduler.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date


@Entity(tableName = "MediaTable")
data class MediaTable(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "descriptionText")
    var descriptionText: String,
    @ColumnInfo(name = "image")
    var imagePath: String,
    @ColumnInfo(name = "time")
    var time: Date // Store time as Date
) : Serializable


