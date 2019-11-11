package com.alokomkar.mediaplay.ui.model

import com.alokomkar.mediaplay.ui.main.data.MediaInfo

class MainDataRepository(private val local : MainDataContract.Local) : MainDataContract.Repository {

    override fun updateMedia(mediaInfo: MediaInfo) {
        local.updateItem(mediaInfo)
    }
}