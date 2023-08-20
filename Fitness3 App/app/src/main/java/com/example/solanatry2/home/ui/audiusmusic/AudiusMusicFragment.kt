package com.example.solanatry2.home.ui.audiusmusic

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.solanatry2.R
import com.example.solanatry2.databinding.FragmentAudiusMusicBinding
import com.example.solanatry2.home.ui.audiusmusic.musicbottomsheet.AudiusMusicBottomSheet
import com.example.solanatry2.home.ui.audiusmusic.recyclerview.AudiusRecyclerViewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AudiusMusicFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAudiusMusicBinding.inflate(layoutInflater)
        val viewModel = ViewModelProvider(requireActivity())[AudiusMusicViewModel::class.java]

        viewModel.showToastMessage.observe(requireActivity()) {message ->
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val result = viewModel.getTrendingTracks()
            withContext(Dispatchers.Main) {
                val myAdapter = AudiusRecyclerViewAdapter(
                    result,
                    requireActivity(),
                    viewModel::playPauseMusic,
                    viewModel::setSelectedPosition,
                    ::openBottomSheet
                )
                binding.myRecyclerview.apply {
                    adapter = myAdapter
                    layoutManager = LinearLayoutManager(requireActivity())
                    setHasFixedSize(true)
                }
            }
        }

        return binding.root
    }

    fun openBottomSheet() {
        val bottomSheet = AudiusMusicBottomSheet()
        bottomSheet.show(requireActivity().supportFragmentManager, "ModalBottomSheet")
    }
}