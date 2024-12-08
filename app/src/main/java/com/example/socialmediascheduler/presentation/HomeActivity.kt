package com.example.socialmediascheduler.presentation

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediascheduler.R
import com.example.socialmediascheduler.data.DatabaseBuilder
import com.example.socialmediascheduler.data.MediaDao
import com.example.socialmediascheduler.data.MediaTable
import com.example.socialmediascheduler.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private var db: MediaDao? = null
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = DatabaseBuilder.getDatabase(this@HomeActivity).dao()
        CoroutineScope(Dispatchers.Main).launch {

            db!!.getMedia().observe(this@HomeActivity) {
                binding.rv.adapter = MainRecycerview(
                    this@HomeActivity, it,
                    deleteLembda = {
                        CoroutineScope(Dispatchers.Main).launch {
                            db!!.deleteMedia(it)
                        }
                    },
                    updateLembda = {
                        CoroutineScope(Dispatchers.Main).launch {
                            db!!.updateMedia(MediaTable(id = it.id, descriptionText = "I am updated now", imagepath = it.imagepath))
                        }
                    }
                )
                binding.rv.layoutManager = LinearLayoutManager(this@HomeActivity)
            }
        }
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this@HomeActivity, MainActivity::class.java))
        }
    }
}