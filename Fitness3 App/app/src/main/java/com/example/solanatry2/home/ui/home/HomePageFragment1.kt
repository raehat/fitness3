package com.example.solanatry2.home

import android.graphics.Color.BLACK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.solanatry2.R
import com.example.solanatry2.databinding.FragmentHomePage1Binding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class HomePageFragment1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomePage1Binding.inflate(layoutInflater)

        val lineEntries = getEntries()
        val lineDataSet = LineDataSet(lineEntries, "")
        lineDataSet.valueTextColor = BLACK;
        lineDataSet.valueTextSize = 18f;

        binding.startWalking.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host_fragment_content_main)
                .navigate(R.id.nav_count_steps)
        }

        binding.activityChart.data = LineData(lineDataSet)
        return binding.root
    }

    private fun getEntries(): ArrayList<Entry> {
        val lineEntries = ArrayList<Entry>()
        lineEntries.add(Entry(2f, 0f))
        lineEntries.add(Entry(5f, 2f))
        lineEntries.add(Entry(24f, 5f))
        lineEntries.add(Entry(23f, 2f))
        lineEntries.add(Entry(12f, 8f))
        return lineEntries
    }
}