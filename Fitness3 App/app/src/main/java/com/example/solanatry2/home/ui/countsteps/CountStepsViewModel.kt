package com.example.solanatry2.home.ui.countsteps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class CountStepsViewModel : ViewModel() {
    var startingValue : Int? = null
    var currentValue : Int = 0
    val stepsCount = MutableLiveData<Int>()
    val backendUrl = "https://fitness3backend--development.gadget.app"

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

    suspend fun requestBackendForNewNft(address : String?) {
        val response = client.post<JsonObject> {
            url("$backendUrl/mint")
            parameter("address", address) // Add the string parameter
        }
    }


    private val client = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.ALL
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                explicitNulls = false
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }
        defaultRequest {
            if (method != HttpMethod.Get) contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }
}