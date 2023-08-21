package com.example.solanatry2.login

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.solanatry2.MyApplication
import com.example.solanatry2.databinding.LoginFragment2Binding
import com.example.solanatry2.home.MainActivity
import java.util.Calendar

class LoginFragment2 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LoginFragment2Binding.inflate(layoutInflater)
        val viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        val dataStoreManager = (requireActivity().applicationContext as MyApplication).dataStoreManager

        binding.dob.setOnClickListener {
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    binding.dob.text =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.nextButton.setOnClickListener {
            with(binding) {
                viewModel.saveLoginDataAndMoveToHomePage(
                    chooseName.text.toString(),
                    dob.text.toString(),
                    weight.text.toString(),
                    height.text.toString(),
                    dataStoreManager
                )
            }
            viewModel.moveToMainActivity.postValue(true)
        }

        return binding.root
    }
}