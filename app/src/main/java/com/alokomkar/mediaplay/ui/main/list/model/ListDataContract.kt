package com.alokomkar.mediaplay.ui.main.list.model

import com.alokomkar.core.networking.Response
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface ListDataContract {

    interface Repository {
        val mediaListResponse : PublishSubject<Response<List<MediaInfo>>>
        val urlResolverResponse : PublishSubject<Response<String>>
        fun fetchAllMedia()
        fun saveAllMedia( mediaList : List<MediaInfo> )
        fun resolveShortUrl( shortUrl: String )
        fun handleError( error : Throwable )
    }

    interface Local {
        fun saveLocalMedia(list : List<MediaInfo>)
        fun getLocalMedia(): Flowable<List<MediaInfo>>
    }

    interface Remote {
        fun getAllMedia(): Single<List<MediaInfo>>
        fun getExpandedUrl( shortUrl : String ): Single<String>
    }
}