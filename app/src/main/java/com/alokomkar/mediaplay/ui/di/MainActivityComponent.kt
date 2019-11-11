package com.alokomkar.mediaplay.ui.di

import android.content.Context
import androidx.room.Room
import com.alokomkar.core.di.CoreComponent
import com.alokomkar.core.networking.Scheduler
import com.alokomkar.mediaplay.BuildConfig
import com.alokomkar.mediaplay.MainActivity
import com.alokomkar.mediaplay.commons.local.MediaDb
import com.alokomkar.mediaplay.ui.model.MainDataContract
import com.alokomkar.mediaplay.ui.model.MainDataRepository
import com.alokomkar.mediaplay.ui.model.MainLocalData
import com.alokomkar.mediaplay.ui.viewmodel.MainViewModelFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@MainActivityScope
@Component(dependencies = [CoreComponent::class], modules = [PlayerModule::class, MainActivityModule::class])
interface MainActivityComponent {
    fun exoPlayer(): SimpleExoPlayer
    fun inject(mainActivity: MainActivity)
}

@Module
class MainActivityModule {

    @Provides
    @MainActivityScope
    fun providesMainViewModelFactory( repository: MainDataContract.Repository, compositeDisposable: CompositeDisposable ) : MainViewModelFactory = MainViewModelFactory( repository, compositeDisposable )

    @Provides
    @MainActivityScope
    fun compositeDisposable() : CompositeDisposable = CompositeDisposable()

    @Provides
    @MainActivityScope
    fun providesMainRepository(local : MainDataContract.Local) : MainDataContract.Repository = MainDataRepository(local)

    @Provides
    @MainActivityScope
    fun postDb(context: Context): MediaDb = Room.databaseBuilder(context, MediaDb::class.java, "${BuildConfig.APPLICATION_ID}.media_database" ).build()

    @Provides
    @MainActivityScope
    fun localData(mediaDb: MediaDb, scheduler: Scheduler): MainDataContract.Local = MainLocalData(mediaDb, scheduler)

}