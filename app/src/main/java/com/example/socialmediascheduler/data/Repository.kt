package com.example.socialmediascheduler.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.Date

class Repository(context: Context) : MediaRepo {
    private var db = DatabaseBuilder.getDatabase(context).dao()
    override suspend fun saveImage(context: Context, uri: Uri): String {
        // Example function to save an image and return its file path
        val file = File(context.filesDir, "image_${System.currentTimeMillis()}.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        file.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        return file.absolutePath
    }

    override suspend fun addImageToDatabase(
        context: Context,
        uri: Uri,
        description: String,
        time: Date,
        mediaObject: (MediaTable) -> Unit
    ) {
        val filePath = saveImage(context, uri)
        val media = MediaTable(descriptionText = description, imagePath = filePath, time = time)
        mediaObject.invoke(media)
    }
}