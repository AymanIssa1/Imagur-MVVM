package com.example.imagurtask.util.extensions

import android.net.Uri
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

fun PlayerView.initializePlayer(videoURL: String, enableAudio: Boolean = false) {
    player = ExoPlayerFactory.newSimpleInstance(
        this.context,
        DefaultTrackSelector(), DefaultLoadControl()
    )

    this.player = player


    val mediaSource = buildMediaSource(videoURL)
    (player as SimpleExoPlayer?)?.prepare(mediaSource, false, false)
    if (!enableAudio)
        (player as SimpleExoPlayer?)?.volume = 0f // disable audio

    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    player.playWhenReady = true
    autoRepeat()
}

fun PlayerView.autoRepeat() {
    player.repeatMode = Player.REPEAT_MODE_ONE
}

fun PlayerView.releasePlayer() {
    if (player != null) {
        player.release()
        player = null
    }
}

fun PlayerView.pausePlayer() {
    player.playWhenReady = false
    player.playbackState
}

fun PlayerView.startPlayer() {
    player.playWhenReady = true
    player.playbackState
}

private fun buildMediaSource(videoURL: String): MediaSource {
    return ProgressiveMediaSource.Factory(
        DefaultHttpDataSourceFactory(
            "exo-player",
            null /* listener */,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true
        )
    ).createMediaSource(Uri.parse(videoURL))
}