package com.example.socialmediascheduler.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.socialmediascheduler.data.DatabaseBuilder
import com.example.socialmediascheduler.data.MediaDao
import com.example.socialmediascheduler.data.MediaTable
import com.example.socialmediascheduler.data.NotificationWorker
import com.example.socialmediascheduler.data.Repository
import com.example.socialmediascheduler.databinding.ActivityMainBinding
import com.example.socialmediascheduler.domain.MediaViewModel
import com.example.socialmediascheduler.domain.MediaViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.OutputStream
import java.sql.Time
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class AddUpdateActivity : AppCompatActivity() {
    private lateinit var repository: Repository
    private var uri: Uri? = null
   private lateinit var selectedDateTime: Calendar
    private lateinit var mediaViewModel: MediaViewModel
    private lateinit var date: Date
    private var luncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        uri = it
    }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent.hasExtra("update")) {
            Toast.makeText(this, "this is the if black ", Toast.LENGTH_SHORT).show()
            val media = intent.getSerializableExtra("update", MediaTable::class.java)
            media?.let {
                println("Description: ${it.descriptionText}")
                println("Image Path: ${it.imagePath}")
                println("Time: ${it.time}")
            }
        }

        mediaViewModel = ViewModelProvider(
            this,
            factory = MediaViewModelFactory(this)
        )[MediaViewModel::class.java]

        repository = Repository(this@AddUpdateActivity)

        binding.addImageButton.setOnClickListener {
            luncher.launch("image/*")
        }

        binding.timeButton.setOnClickListener {
            binding.timeButton.setOnClickListener {
                // Get the current date and time
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                // Show DatePickerDialog
                val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    // After selecting a date, show the TimePickerDialog
                    val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                        // Create a Calendar instance for the selected date and time

                        selectedDateTime = Calendar.getInstance().apply {
                            set(Calendar.YEAR, selectedYear)
                            set(Calendar.MONTH, selectedMonth)
                            set(Calendar.DAY_OF_MONTH, selectedDay)
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                            date=selectedDateTime.time
                        val currentTime = Calendar.getInstance()

                        // Adjust for scheduling the next day if the selected time has already passed
                        if (selectedDateTime.before(currentTime)) {
                            selectedDateTime.add(Calendar.DAY_OF_YEAR, 1)
                        }

                        // Calculate the delay in milliseconds
                        val delay = selectedDateTime.timeInMillis - currentTime.timeInMillis

                        // Create a OneTimeWorkRequest with the calculated delay
                        val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                            .build()

                        // Enqueue the work request
                        WorkManager.getInstance(this).enqueue(notificationWorkRequest)

                        // Log and show the selected date and time
                        Log.d("Selected DateTime", "Scheduled for: ${selectedDateTime.time}")
                        Toast.makeText(this, "Notification scheduled for: ${selectedDateTime.time}", Toast.LENGTH_SHORT).show()
                    }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false)

                    // Show Time Picker Dialog
                    timePickerDialog.show()
                }, year, month, day)

                // Show Date Picker Dialog
                datePickerDialog.show()
            }

        }
        binding.saveButton.setOnClickListener {
            val description = binding.editDescription.text.toString()
            if (uri == null) {
                Toast.makeText(this, "Data is needed", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                repository.addImageToDatabase(this@AddUpdateActivity, uri!!, description, date) {
                    mediaViewModel.insertMedia(it)
                }
            }
            Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@AddUpdateActivity, HomeActivity::class.java))


        }


    }


}