package com.alokomkar.mediaplay.ui.main.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alokomkar.core.extensions.toLiveData
import com.alokomkar.core.networking.Response
import com.alokomkar.mediaplay.commons.ComponentHolder
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import com.alokomkar.mediaplay.ui.main.list.model.ListDataContract
import io.reactivex.disposables.CompositeDisposable

class ListViewModel(private val repo: ListDataContract.Repository,
                    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    val mediaListResponse: LiveData<Response<List<MediaInfo>>> by lazy {
        //Convert publish subject to livedata
        repo.mediaListResponse.toLiveData(compositeDisposable)
    }

    val urlResolverResponse: LiveData<Response<String>> by lazy {
        repo.urlResolverResponse.toLiveData(compositeDisposable)
    }

    fun getMedia() {
        repo.fetchAllMedia()
    }

    fun resolveShortUrl( shortUrl : String ) {
        repo.resolveShortUrl(shortUrl)
    }

    override fun onCleared() {
        super.onCleared()
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        ComponentHolder.destroyListComponent()
    }


}