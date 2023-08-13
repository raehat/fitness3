package com.example.solanatry2.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.fragment.app.Fragment
import com.example.solanatry2.databinding.LoginFragment1Binding

class LoginFragment1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LoginFragment1Binding.inflate(layoutInflater)

        val radiatingView = binding.radiatingView
        val scaleAnimation = ScaleAnimation(
            2f, 3f, // X-axis scale from 1 to 8
            2f, 3f, // Y-axis scale from 1 to 8
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point (center X)
            Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point (center Y)
        ).apply {
            duration = 1500
            repeatCount = Animation.INFINITE // Repeat the animation infinitely
            repeatMode = Animation.RESTART   // Reset the animation after each repetition
            interpolator = AccelerateInterpolator() // Adjust the interpolator as needed
        }

        radiatingView.startAnimation(scaleAnimation)

        return binding.root
    }
}