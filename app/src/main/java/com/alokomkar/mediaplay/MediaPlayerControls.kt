package com.alokomkar.mediaplay

import com.alokomkar.mediaplay.ui.main.data.MediaInfo

interface MediaPlayerControls {
    fun isPlaying() : Boolean
    fun playMedia( mediaInfo: MediaInfo )
    fun pauseMedia()
    fun resumeMedia()
    fun stopMedia()
}