package com.alokomkar.mediaplay.ui.main.list.model

import com.alokomkar.mediaplay.commons.remote.MediaAPIService
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import io.reactivex.Single

class ListRemoteData( private val service: MediaAPIService) : ListDataContract.Remote {
    override fun getAllMedia(): Single<List<MediaInfo>> = service.getAllMedia()
    override fun getExpandedUrl(shortUrl: String): Single<String> = service.getExpandedUrl(shortUrl)
}