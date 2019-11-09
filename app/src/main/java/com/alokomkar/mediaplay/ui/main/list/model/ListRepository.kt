package com.alokomkar.mediaplay.ui.main.list.model

import com.alokomkar.core.extensions.*
import com.alokomkar.core.networking.Response
import com.alokomkar.core.networking.Scheduler
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class ListRepository(
    private val remote: ListDataContract.Remote,
    private val local: ListDataContract.Local,
    private val scheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable
) : ListDataContract.Repository {

    override fun saveAllMedia( mediaList : List<MediaInfo> ) {
        local.saveLocalMedia(mediaList)
    }

    override fun updateMedia(mediaInfo: MediaInfo) {
        local.updateItem(mediaInfo)
    }

    override val mediaListResponse: PublishSubject<Response<List<MediaInfo>>>
            =  PublishSubject.create<Response<List<MediaInfo>>>()

    override val urlResolverResponse: PublishSubject<Response<String>>
            =  PublishSubject.create<Response<String>>()

    override fun fetchAllMedia() {
        mediaListResponse.loading(true)
        local.getLocalMedia()
            .performOnBackOutOnMain(scheduler)
            .doAfterNext {
                if(it.isEmpty()) {
                    fetchFromRemote()
                }
            }
            .subscribe(
                { schedulesList -> if( schedulesList != null ) mediaListResponse.success(schedulesList) },
                { error -> handleError(error) })
            .addTo(compositeDisposable)

    }

    private fun fetchFromRemote() {
        mediaListResponse.loading(true)
        remote.getAllMedia()
            .performOnBackOutOnMain(scheduler)
            .subscribe(
                { schedulesList -> if( schedulesList != null ) {
                    mediaListResponse.success(schedulesList)
                    saveAllMedia(schedulesList)
                }
                },
                { error -> handleError(error) })
            .addTo(compositeDisposable)
    }

    override fun resolveShortUrl(shortUrl: String) {
        urlResolverResponse.loading(true)
        remote.getExpandedUrl(shortUrl)
            .performOnBackOutOnMain(scheduler)
            .subscribe (
                {  url -> urlResolverResponse.success(url) },
                { error -> handleError(error) }
            ).addTo(compositeDisposable)
    }

    override fun handleError(error: Throwable) {
        mediaListResponse.failed(error)
    }
}