package com.alokomkar.mediaplay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alokomkar.mediaplay.ui.model.MainDataContract
import io.reactivex.disposables.CompositeDisposable

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val repository: MainDataContract.Repository,
                           private val compositeDisposable: CompositeDisposable
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository, compositeDisposable) as T
    }
}