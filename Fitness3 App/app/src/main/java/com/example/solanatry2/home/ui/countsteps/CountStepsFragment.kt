package com.example.solanatry2.home.ui.countsteps

import DataStoreManager
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.solanatry2.MyApplication
import com.example.solanatry2.R
import com.example.solanatry2.databinding.FragmentCountStepsBinding
import com.example.solanatry2.servicesandbroadcastreceivers.StepCounterService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CountStepsFragment : Fragment() {

    private val serviceIntent by lazy { Intent(requireActivity(), StepCounterService::class.java) }
    private val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 123
    private var serviceRunning = false
    lateinit var countStepsViewModel: CountStepsViewModel
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        countStepsViewModel =
            ViewModelProvider(this)[CountStepsViewModel::class.java]

        dataStoreManager = (requireActivity().applicationContext as MyApplication).dataStoreManager
        val binding = FragmentCountStepsBinding.inflate(inflater, container, false)
        val dataStoreManager = (requireActivity().applicationContext as MyApplication).dataStoreManager

        dataStoreManager.getStepsCompleted().asLiveData().observe(requireActivity()) {
            binding.circularProgress.maxProgress = 10000.0
            binding.circularProgress.setCurrentProgress(it?.toDouble() ?: 0.0)
            if (it == null || it < 0) {
                binding.stepsCountText.text = "0 out of 10,000 steps completed"
            } else {
                if (it > 50) {
                    mintNewNft()
                }
                binding.stepsCountText.text = "$it out of 10,000 steps completed"
            }
        }

        binding.startCountingSteps.setOnClickListener {
            if (serviceRunning) {
                Toast.makeText(requireContext(), "Service already running", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            serviceRunning = true
            requireActivity().registerReceiver(countStepsViewM odel.stepsReceiver, IntentFilter("com.example.solanatry2.CUSTOM_INTENT"))
            Toast.makeText(requireContext(), "Started counting steps!", Toast.LENGTH_LONG).show()
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    PERMISSION_REQUEST_ACTIVITY_RECOGNITION)
            } else {
                try {
                    requireActivity().startService(serviceIntent)
                } catch (e: Exception) {
                    Log.d("TAG", e.toString())
                }
            }
        }

        binding.stopCountingSteps.setOnClickListener {
            if (!serviceRunning) {
                Toast.makeText(requireContext(), "Service not running", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            serviceRunning = false
            requireActivity().unregisterReceiver(countStepsViewModel.stepsReceiver)
            Toast.makeText(requireContext(), "Stopped counting steps!", Toast.LENGTH_LONG).show()
            countStepsViewModel.startingValue = null
            requireActivity().stopService(serviceIntent)
        }

        return binding.root
    }

    private fun mintNewNft() {
        val builder = AlertDialog.Builder(requireActivity())
        val layout = layoutInflater.inflate(R.layout.you_got_nft, null)
        builder.setView(layout)
        builder.setNeutralButton("OK", null)
        builder.create().show()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                countStepsViewModel.requestBackendForNewNft(dataStoreManager.getPublicKey().first())
            } catch (e: Exception) {
                Log.d("TAG", e.stackTrace.toString())
            }
        }
    }
}