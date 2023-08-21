package com.example.solanatry2.home.ui.audiusmusic.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.example.solanatry2.databinding.AudiusMusicCardBinding
import com.example.solanatry2.home.ui.audiusmusic.AudiusMusicViewModel
import com.example.solanatry2.home.ui.audiusmusic.audiusdataclass.AudiusData
import com.example.solanatry2.home.ui.audiusmusic.musicbottomsheet.AudiusMusicBottomSheet
import org.romancha.playpause.PlayPauseView

class AudiusRecyclerViewAdapter(
    private val audiusDataClass: AudiusData,
    private val context: FragmentActivity,
    private val playMusicCallBack : (AudiusMusicViewModel.MusicState, String) -> Unit,
    private val setSelectedPositionCallback : (Int) -> Unit,
    private val openBottomSheet: (PlayPauseView) -> Unit
) : RecyclerView.Adapter<AudiusRecyclerViewAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val binding = AudiusMusicCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setSelectedPositionCallback(position)
        holder.musicName.text = audiusDataClass.data[position].title
        holder.musicDes.text = audiusDataClass.data[position].description
        holder.musicPlayPause.setOnClickListener {
            holder.musicPlayPause.toggle()
            if (holder.musicPlayPause.onPlaying()) {
                playMusicCallBack(AudiusMusicViewModel.MusicState.PLAYING, audiusDataClass.data[position].id)
            } else if (holder.musicPlayPause.onPause()) {
                playMusicCallBack(AudiusMusicViewModel.MusicState.PAUSED, audiusDataClass.data[position].id)
            }
        }
        Glide
            .with(context)
            .load(audiusDataClass.data[position].artwork?.n150x150)
            .centerCrop()
            .into(holder.musicImg)

        holder.itemView.setOnClickListener {
            openBottomSheet(holder.musicPlayPause)
        }

    }
    override fun getItemCount(): Int {
        return audiusDataClass.data.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(binding: AudiusMusicCardBinding) : RecyclerView.ViewHolder(binding.root) {
        val musicImg = binding.musicImg
        val musicName = binding.musicName
        val musicDes = binding.musicDesc
        val musicPlayPause = binding.musicPlay
    }
}
