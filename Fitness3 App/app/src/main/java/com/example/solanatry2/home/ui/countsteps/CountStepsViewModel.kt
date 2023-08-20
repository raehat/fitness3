package com.example.solanatry2.home.ui.countsteps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountStepsViewModel : ViewModel() {
    var startingValue : Int? = null
    var currentValue : Int = 0
    val stepsCount = MutableLiveData<Int>()

    val stepsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val steps = intent.extras?.getInt("steps") ?: 0
            if (startingValue == null)
                startingValue = steps
            else {
                currentValue = steps - (startingValue ?: 0)
                stepsCount.postValue(currentValue)
            }
        }
    }
}