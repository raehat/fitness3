package com.example.solanatry2.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.solanatry2.databinding.FragmentOnBoarding1Binding

class OnBoardingFragment1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOnBoarding1Binding.inflate(layoutInflater)
        return binding.root
    }
}