package com.example.socialmediascheduler.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediascheduler.data.DatabaseBuilder
import com.example.socialmediascheduler.data.MediaTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaViewModel(context: Context) : ViewModel() {

    private var db = DatabaseBuilder.getDatabase(context = context).dao()

    val mediaList: LiveData<List<MediaTable>> get() = db.getMedia()


    fun insertMedia(mediaTable: MediaTable) {
        viewModelScope.launch {
            db.insertData(mediaTable)
        }
    }

    fun updateMedia(mediaTable: MediaTable) {
        viewModelScope.launch {
            db.updateMedia(mediaTable)
        }
    }

    fun deleteMedia(mediaTable: MediaTable) {
        viewModelScope.launch {
            db.deleteMedia(mediaTable)
        }
    }


}