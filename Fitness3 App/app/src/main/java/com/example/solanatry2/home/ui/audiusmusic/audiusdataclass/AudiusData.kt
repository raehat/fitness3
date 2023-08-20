package com.example.solanatry2.home.ui.audiusmusic.audiusdataclass
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AudiusData(
    val data: List<Daum>,
)

@Serializable
data class Daum(
    val artwork: Artwork?,
    val description: String?,
    val genre: String,
    val id: String,
    @SerialName("track_cid")
    val track_cid: String,
    @SerialName("preview_cid")
    val preview_cid: String?,
    val mood: String?,
    @SerialName("release_date")
    val release_date: String?,
    @SerialName("remix_of")
    val remix_of: RemixOf,
    @SerialName("repost_count")
    val repost_count: Long,
    @SerialName("favorite_count")
    val favorite_count: Long,
    val tags: String?,
    val title: String,
    val user: User,
    val duration: Long,
    val downloadable: Boolean,
    @SerialName("play_count")
    val playCount: Long,
    val permalink: String,
    @SerialName("is_streamable")
    val isStreamable: Boolean,
)

@Serializable
data class Artwork(
    @SerialName("150x150")
    val n150x150: String,
    @SerialName("480x480")
    val n480x480: String,
    @SerialName("1000x1000")
    val n1000x1000: String,
)

@Serializable
data class RemixOf(
    val tracks: List<Track>?,
)

@Serializable
data class Track(
    @SerialName("parent_track_id")
    val parent_track_id: String,
)

@Serializable
data class User(
    @SerialName("album_count")
    val album_count: Long,
    @SerialName("artist_pick_track_id")
    val artistPickTrackId: String?,
    val bio: String?,
    @SerialName("cover_photo")
    val coverPhoto: CoverPhoto?,
    @SerialName("followee_count")
    val followee_count: Long,
    @SerialName("follower_count")
    val follower_count: Long,
    @SerialName("does_follow_current_user")
    val does_follow_current_user: Boolean,
    val handle: String,
    val id: String,
    @SerialName("is_verified")
    val is_verified: Boolean,
    val location: String?,
    val name: String,
    @SerialName("playlist_count")
    val playlist_count: Long,
    @SerialName("profile_picture")
    val profile_picture: ProfilePicture,
    @SerialName("repost_count")
    val repost_count: Long,
    @SerialName("track_count")
    val track_count: Long,
    @SerialName("is_deactivated")
    val isDeactivated: Boolean,
    @SerialName("is_available")
    val is_available: Boolean,
    @SerialName("erc_wallet")
    val erc_wallet: String,
    @SerialName("spl_wallet")
    val splWallet: String?,
    @SerialName("supporter_count")
    val supporterCount: Long,
    @SerialName("supporting_count")
    val supporting_count: Long,
    @SerialName("total_audio_balance")
    val total_audio_balance: Long,
)

@Serializable
data class CoverPhoto(
    @SerialName("640x")
    val n640x: String,
    @SerialName("2000x")
    val n2000x: String,
)

@Serializable
data class ProfilePicture(
    @SerialName("150x150")
    val n150x150: String,
    @SerialName("480x480")
    val n480x480: String,
    @SerialName("1000x1000")
    val n1000x1000: String,
)
