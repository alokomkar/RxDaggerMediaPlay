package com.alokomkar.mediaplay.ui.main.list.di

import android.content.Context
import androidx.room.Room
import com.alokomkar.core.di.CoreComponent
import com.alokomkar.core.networking.Scheduler
import com.alokomkar.mediaplay.BuildConfig
import com.alokomkar.mediaplay.commons.local.MediaDb
import com.alokomkar.mediaplay.commons.remote.MediaAPIService
import com.alokomkar.mediaplay.ui.main.list.ListFragment
import com.alokomkar.mediaplay.ui.main.list.MediaInfoListAdapter
import com.alokomkar.mediaplay.ui.main.list.model.ListDataContract
import com.alokomkar.mediaplay.ui.main.list.model.ListLocalData
import com.alokomkar.mediaplay.ui.main.list.model.ListRemoteData
import com.alokomkar.mediaplay.ui.main.list.model.ListRepository
import com.alokomkar.mediaplay.ui.main.list.viewmodel.ListViewModelFactory
import com.squareup.picasso.Picasso
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit

@ListScope
@Component(dependencies = [CoreComponent::class], modules = [ListModule::class])
interface ListComponent {
    fun picasso(): Picasso
    fun mediaDb(): MediaDb
    fun launchScheduleService(): MediaAPIService
    fun scheduler(): Scheduler
    fun inject(fragment: ListFragment)
}

@Module
class ListModule {

    @Provides
    @ListScope
    fun adapter(picasso: Picasso): MediaInfoListAdapter = MediaInfoListAdapter(picasso)

    /*ViewModel*/
    @Provides
    @ListScope
    fun listViewModelFactory(repository: ListDataContract.Repository, compositeDisposable: CompositeDisposable): ListViewModelFactory = ListViewModelFactory(repository,compositeDisposable)

    /*Repository*/
    @Provides
    @ListScope
    fun listRepo(remote: ListDataContract.Remote, local: ListDataContract.Local, scheduler: Scheduler, compositeDisposable: CompositeDisposable): ListDataContract.Repository = ListRepository(remote, local, scheduler, compositeDisposable)

    @Provides
    @ListScope
    fun remoteData(scheduleService: MediaAPIService): ListDataContract.Remote = ListRemoteData(scheduleService)

    @Provides
    @ListScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    @ListScope
    fun localData(mediaDb: MediaDb, scheduler: Scheduler): ListDataContract.Local = ListLocalData(mediaDb, scheduler)

    @Provides
    @ListScope
    fun postDb(context: Context): MediaDb = Room.databaseBuilder(context, MediaDb::class.java, "${BuildConfig.APPLICATION_ID}.media_database" ).build()

    @Provides
    @ListScope
    fun postService(retrofit: Retrofit): MediaAPIService = retrofit.create(MediaAPIService::class.java)

}