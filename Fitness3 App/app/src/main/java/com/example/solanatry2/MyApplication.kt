package com.example.solanatry2

import DataStoreManager
import android.app.Application
import androidx.multidex.MultiDexApplication

class MyApplication : Application() {

    lateinit var dataStoreManager: DataStoreManager
    override fun onCreate() {
        super.onCreate()
        dataStoreManager = DataStoreManager(applicationContext)
    }

}
