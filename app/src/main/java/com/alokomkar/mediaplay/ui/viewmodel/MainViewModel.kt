package com.alokomkar.mediaplay.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import com.alokomkar.mediaplay.ui.model.MainDataContract
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(private val repo: MainDataContract.Repository,
                    private val compositeDisposable: CompositeDisposable) : ViewModel() {

    fun updateMedia( item : MediaInfo ) {
        repo.updateMedia(mediaInfo = item)
    }
}