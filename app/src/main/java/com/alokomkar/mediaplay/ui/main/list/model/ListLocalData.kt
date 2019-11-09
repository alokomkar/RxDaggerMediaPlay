package com.alokomkar.mediaplay.ui.main.list.model

import com.alokomkar.core.extensions.performOnBack
import com.alokomkar.core.networking.Scheduler
import com.alokomkar.mediaplay.commons.local.MediaDb
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import io.reactivex.Completable
import io.reactivex.Flowable

class ListLocalData(private val mediaDb : MediaDb, private val scheduler: Scheduler ) : ListDataContract.Local {

    override fun saveLocalMedia(list: List<MediaInfo>) {
        Completable.fromAction {
            mediaDb.mediaInfoDao().upsertAll(list)
        }
            .performOnBack(scheduler)
            .subscribe()

    }

    override fun updateItem(mediaInfo: MediaInfo) {
        Completable.fromAction {
            mediaDb.mediaInfoDao().updateMedia(mediaInfo)
        }
            .performOnBack(scheduler)
            .subscribe()
    }

    override fun getLocalMedia(): Flowable<List<MediaInfo>>
            = mediaDb.mediaInfoDao().getAll()
}