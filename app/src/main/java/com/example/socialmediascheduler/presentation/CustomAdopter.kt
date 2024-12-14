package com.example.socialmediascheduler.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediascheduler.data.MediaTable
import com.example.socialmediascheduler.databinding.SampleBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


class CustomAdopter(
    private val context: Context,
    private val list: List<MediaTable>,
    private var deleteLembda: (MediaTable) -> Unit,
    private var updateLembda: (MediaTable) -> Unit
) :
    RecyclerView.Adapter<CustomAdopter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SampleBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.textview.text = list[position].descriptionText
        Glide.with(context)
            .load(File(list[position].imagePath)) // Load the image from the file path
            .into(holder.binding.imageview)

        holder.itemView.setOnClickListener {
            deleteLembda.invoke(list[position])
        }
        holder.binding.imageview.setOnClickListener {
            updateLembda.invoke(list[position])
        }
        val dateFormat = SimpleDateFormat("HH:mm yyyy-MM-dd", Locale.getDefault())
        holder.binding.timeview.text = dateFormat.format(list[position].time) // Display the formatted time

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: SampleBinding) :
        RecyclerView.ViewHolder(binding.root)
}