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
import com.example.solanatry2.databinding.LoginFragment2Binding

class LoginFragment2 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LoginFragment2Binding.inflate(layoutInflater)
        return binding.root
    }
}