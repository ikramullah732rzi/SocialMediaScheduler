package com.example.socialmediascheduler.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.room.RoomDatabase
import com.example.socialmediascheduler.data.DatabaseBuilder
import com.example.socialmediascheduler.data.MediaDao
import com.example.socialmediascheduler.data.MediaDatabase
import com.example.socialmediascheduler.data.MediaTable
import com.example.socialmediascheduler.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {
    private var uri: Uri? = null
     private var db : MediaDao?=null
    private var luncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        uri = it
    }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addImageButton.setOnClickListener {
            luncher.launch("image/*")
        }

       db = DatabaseBuilder.getDatabase(this).dao()

        binding.saveButton.setOnClickListener {
            val description = binding.editDescription.text.toString()
            if (uri == null) {
                return@setOnClickListener
            }
            GlobalScope.launch {
               addImageToDatabase(this@MainActivity,uri!!,description)
            }
            Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))


        }

    }

    fun saveImage(context: Context, uri: Uri): String {
        // Example function to save an image and return its file path
        val file = File(context.filesDir, "image_${System.currentTimeMillis()}.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        file.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        return file.absolutePath
    }
    suspend fun addImageToDatabase(context: Context, uri: Uri,description : String) {
        val filePath = saveImage(context, uri)
        val media = MediaTable(descriptionText = description, imagepath = filePath )
        db?.insertData(media)

    }
}