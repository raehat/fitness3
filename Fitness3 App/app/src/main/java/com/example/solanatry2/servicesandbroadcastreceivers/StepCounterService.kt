package com.example.solanatry2.servicesandbroadcastreceivers

import DataStoreManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.asLiveData
import com.example.solanatry2.MyApplication
import com.example.solanatry2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StepCounterService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepCounter: Int = 0
    lateinit var dataStoreManager : DataStoreManager
    private var startingValue : Int? = null

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        dataStoreManager = (applicationContext as MyApplication).dataStoreManager
        val notification = Notification.Builder(this, "CHANNEL_ID")
            .setContentTitle("Step Counter Service")
            .setContentText("Counting steps...")
            .setSmallIcon(R.drawable.solana_sol_icon)
            .build()

        startForeground(1337, notification)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            stepCounter = event.values[0].toInt()
            if (startingValue == null) {
                startingValue = stepCounter
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    val stepsCompleted = dataStoreManager.getStepsCompleted().first()
                    if (stepsCompleted != null) {
                        dataStoreManager.setStepsCompleted(stepsCompleted + (stepCounter - (startingValue ?: 0)))
                    } else {
                        dataStoreManager.setStepsCompleted(((startingValue ?: 0) - stepCounter))
                    }
                }
            }
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "Step Counter Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}
