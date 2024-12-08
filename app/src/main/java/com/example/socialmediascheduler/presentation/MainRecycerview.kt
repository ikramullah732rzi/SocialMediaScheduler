package com.example.socialmediascheduler.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediascheduler.data.MediaTable
import com.example.socialmediascheduler.databinding.SampleBinding
import java.io.File
import java.io.InputStream


class MainRecycerview(
    val context: Context,
    val list: List<MediaTable>,
    var deleteLembda: (MediaTable) -> Unit,
    var updateLembda: (MediaTable) -> Unit
) :
    RecyclerView.Adapter<MainRecycerview.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SampleBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.textview.text = list[position].descriptionText
        Glide.with(context)
            .load(File(list[position].imagepath)) // Load the image from the file path
            .into(holder.binding.imageview)

        holder.itemView.setOnClickListener {
            deleteLembda.invoke(list[position])
        }
        holder.binding.imageview.setOnClickListener {
            updateLembda.invoke(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: SampleBinding) :
        RecyclerView.ViewHolder(binding.root)
}