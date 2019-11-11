package com.alokomkar.mediaplay.ui.model

import com.alokomkar.mediaplay.ui.main.data.MediaInfo

interface MainDataContract {
    interface Repository {
        fun updateMedia( mediaInfo: MediaInfo)
    }

    interface Local {
        fun updateItem( mediaInfo: MediaInfo )
    }
}