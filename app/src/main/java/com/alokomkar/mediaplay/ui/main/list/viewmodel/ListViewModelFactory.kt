package com.alokomkar.mediaplay.ui.main.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alokomkar.mediaplay.ui.main.list.model.ListDataContract
import io.reactivex.disposables.CompositeDisposable

@Suppress("UNCHECKED_CAST")
class ListViewModelFactory(private val repository: ListDataContract.Repository,
                           private val compositeDisposable: CompositeDisposable
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListViewModel(repository, compositeDisposable) as T
    }
}