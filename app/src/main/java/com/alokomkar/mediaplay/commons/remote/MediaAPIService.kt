package com.alokomkar.mediaplay.commons.remote

import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface MediaAPIService {
    @GET("studio")
    fun getAllMedia(): Single<List<MediaInfo>>

    @GET
    fun getExpandedUrl( @Url shortUrl : String ): Single<String>
}