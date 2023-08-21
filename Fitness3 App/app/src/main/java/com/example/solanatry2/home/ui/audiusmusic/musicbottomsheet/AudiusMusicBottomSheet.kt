package com.example.solanatry2.home.ui.audiusmusic.musicbottomsheet

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.solanatry2.R
import com.example.solanatry2.databinding.BottomSheetMusicPlayerBinding
import com.example.solanatry2.home.ui.audiusmusic.AudiusMusicViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.romancha.playpause.PlayPauseView

class AudiusMusicBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel : AudiusMusicViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = BottomSheetMusicPlayerBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[AudiusMusicViewModel::class.java]

        startGradientAnimation(binding.backgroundLayout)

        updateProgressBar(binding.musicProgressBar)

        handlePlayPause(binding.playPauseBottomSheet)

        Glide
            .with(requireActivity())
            .load(viewModel.audiusData.data[viewModel.selectedPositionOfResult].artwork?.n480x480)
            .centerCrop()
            .into(binding.musicImg)

        binding.musicTitle.text = viewModel.audiusData.data[viewModel.selectedPositionOfResult].title
        binding.singerName.text = viewModel.audiusData.data[viewModel.selectedPositionOfResult].user.name

        return binding.root
    }

    private fun handlePlayPause(playPauseBottomSheet: PlayPauseView) {
        if (viewModel.mediaPlayer.isPlaying) {
            playPauseBottomSheet.toggle()
        }
        playPauseBottomSheet.setOnClickListener {
            playPauseBottomSheet.toggle()
            if (playPauseBottomSheet.onPlaying()) {
                viewModel.playPauseMusic(AudiusMusicViewModel.MusicState.PLAYING)
            } else if (playPauseBottomSheet.onPause()) {
                viewModel.playPauseMusic(AudiusMusicViewModel.MusicState.PAUSED)
            }
        }
    }

    private fun updateProgressBar(musicProgressBar: ProgressBar) {
        var progress = (viewModel.mediaPlayer.currentPosition / viewModel.mediaPlayer.duration.coerceAtLeast(1)) * 100
        musicProgressBar.progress = progress
        CoroutineScope(Dispatchers.Main).launch {
            while (progress < 100) {
                progress = (viewModel.mediaPlayer.currentPosition / viewModel.mediaPlayer.duration.coerceAtLeast(1)) * 100
                musicProgressBar.progress = progress
                delay(500)
            }
        }
    }

    private fun startGradientAnimation(backgroundLayout: LinearLayout) {
        val colorAnimator = ValueAnimator.ofObject(
            ArgbEvaluator(),
            Color.parseColor("#125480"),
            Color.parseColor("#0D7037")
        )
        colorAnimator.duration = 5000 // Change duration as needed
        colorAnimator.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            backgroundLayout.setBackgroundColor(color)
        }
        colorAnimator.repeatCount = ValueAnimator.INFINITE
        colorAnimator.repeatMode = ValueAnimator.REVERSE
        colorAnimator.start()
    }
}
