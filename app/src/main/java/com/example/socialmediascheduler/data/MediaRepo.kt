package com.example.socialmediascheduler.data

import android.content.Context
import android.net.Uri
import java.util.Date

interface MediaRepo {
   suspend fun saveImage(context: Context, uri: Uri):String
    suspend fun addImageToDatabase(context: Context, uri: Uri, description: String,time : Date , mediaObject: (MediaTable) -> Unit)
}