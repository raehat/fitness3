package com.example.solanatry2.home

import DataStoreManager
import android.content.Intent
import android.graphics.Color.BLACK
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import com.example.solanatry2.MyApplication
import com.example.solanatry2.R
import com.example.solanatry2.databinding.FragmentHomePage1Binding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.pow


class HomePageFragment1 : Fragment() {

    private lateinit var dataStoreManager : DataStoreManager
    private val BMI_KNOW_MORE_URL = "https://en.wikipedia.org/wiki/Body_mass_index"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomePage1Binding.inflate(layoutInflater)
        dataStoreManager = (requireActivity().applicationContext as MyApplication).dataStoreManager

        dataStoreManager.getName().asLiveData().observe(requireActivity()) { name ->
            binding.userName.text = name
        }

        binding.knowMoreButton.setOnClickListener {
            openUrl(BMI_KNOW_MORE_URL)
        }

        val lineEntries = getEntries()
        val lineDataSet = LineDataSet(lineEntries, "")
        lineDataSet.valueTextColor = BLACK;
        lineDataSet.valueTextSize = 18f;

        CoroutineScope(Dispatchers.Main).launch {
            val weight = dataStoreManager.getWeight().first()?.toDouble()
            val height = dataStoreManager.getHeight().first()?.toDouble()
            if (weight != null && height != null) {
                val bmi = weight / (height / 100).pow(2)
                val bmiFormatted = String.format("%.2f", bmi)
                binding.bmiText.text = "Your BMI is $bmiFormatted. BMI should ideally lie between 19 and 25"
            }
        }

        binding.startWalking.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host_fragment_content_main)
                .navigate(R.id.nav_count_steps)
        }

        binding.activityChart.data = LineData(lineDataSet)
        return binding.root
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
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