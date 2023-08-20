package com.example.solanatry2.home.ui.audiusmusic

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.solanatry2.home.ui.audiusmusic.audiusdataclass.AudiusData
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import java.io.IOException


class AudiusMusicViewModel : ViewModel() {
    private val TRENDING_TRACKS_URL = "https://audius-metadata-3.figment.io/v1/tracks/trending"
    private var mediaPlayer : MediaPlayer = MediaPlayer()
    private var musicCurrId : String = ""
    var selectedPositionOfResult : Int = -1
    lateinit var audiusData : AudiusData
    val showToastMessage = MutableLiveData<String>()

    suspend fun getTrendingTracks(): AudiusData {
        val response = client.get<AudiusData> {
            url(TRENDING_TRACKS_URL)
        }
        audiusData = response
        return response
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

    fun playPauseMusic(musicState: MusicState, idMusic: String) {
        when (musicState) {
            MusicState.PLAYING -> handleStateWhenMusicPlaying(idMusic)
            MusicState.PAUSED -> handleStateWhenMusicPaused(idMusic)
        }
    }

    private fun handleStateWhenMusicPlaying(idMusic: String) {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                if (musicCurrId == idMusic) {
                    mediaPlayer.start()
                } else {
                    showToastMessage.postValue("Stop the previous song to play this")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun playNewMusic(idMusic: String) {
        musicCurrId = idMusic
        mediaPlayer.reset()

        mediaPlayer.setDataSource("https://audius-metadata-3.figment.io/v1/tracks/${idMusic}/stream")
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    private fun handleStateWhenMusicPaused(idMusic: String) {
        if (mediaPlayer.isPlaying) {
            if (musicCurrId == idMusic) {
                mediaPlayer.pause();
            } else {
                playNewMusic(idMusic)
            }
        }
    }

    fun setSelectedPosition(value: Int) {
        selectedPositionOfResult = value
    }

    enum class MusicState {
        PLAYING,
        PAUSED
    }
}