package com.example.socialmediascheduler.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediascheduler.data.DatabaseBuilder
import com.example.socialmediascheduler.data.MediaDao
import com.example.socialmediascheduler.databinding.ActivityHomeBinding
import com.example.socialmediascheduler.domain.MediaViewModel
import com.example.socialmediascheduler.domain.MediaViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var mediaViewModel: MediaViewModel
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mediaViewModel = ViewModelProvider(
            this,
            factory = MediaViewModelFactory(this)
        )[MediaViewModel::class.java]


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) { ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 12)
        }
        mediaViewModel.mediaList.observe(this) {
            Log.d("msg", "onCreate: list size ${it.size}")
            binding.rv.adapter = CustomAdopter(this, it,
                deleteLembda = { mediaTable -> mediaViewModel.deleteMedia(mediaTable) },
                updateLembda = { updateMediaTable ->
                    val intent = Intent(this, AddUpdateActivity::class.java)
                    intent.putExtra("update", updateMediaTable)
                    startActivity(intent)
                })
            binding.rv.layoutManager = LinearLayoutManager(this)
        }

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddUpdateActivity::class.java))
        }
    }



}