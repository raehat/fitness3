package com.example.solanatry2.home.ui.audiusmusic.musicbottomsheet

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.solanatry2.R
import com.example.solanatry2.databinding.BottomSheetMusicPlayerBinding
import com.example.solanatry2.home.ui.audiusmusic.AudiusMusicViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AudiusMusicBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = BottomSheetMusicPlayerBinding.inflate(layoutInflater)
        val viewModel = ViewModelProvider(requireActivity())[AudiusMusicViewModel::class.java]

        startGradientAnimation(binding.backgroundLayout)

        Glide
            .with(requireActivity())
            .load(viewModel.audiusData.data[viewModel.selectedPositionOfResult].artwork?.n480x480)
            .centerCrop()
            .into(binding.musicImg)

        binding.musicTitle.text = viewModel.audiusData.data[viewModel.selectedPositionOfResult].title

        return binding.root
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
