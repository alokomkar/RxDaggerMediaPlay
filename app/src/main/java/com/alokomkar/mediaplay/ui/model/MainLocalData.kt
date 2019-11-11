package com.alokomkar.mediaplay.ui.model

import com.alokomkar.core.extensions.performOnBack
import com.alokomkar.core.networking.Scheduler
import com.alokomkar.mediaplay.commons.local.MediaDb
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import io.reactivex.Completable

class MainLocalData(private val mediaDb : MediaDb,
                    private val scheduler: Scheduler) : MainDataContract.Local {

    override fun updateItem(mediaInfo: MediaInfo) {
        Completable.fromAction {
            mediaDb.mediaInfoDao().updateMedia(mediaInfo)
        }
            .performOnBack(scheduler)
            .subscribe()
    }

}