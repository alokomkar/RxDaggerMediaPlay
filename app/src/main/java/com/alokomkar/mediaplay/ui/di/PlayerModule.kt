package com.alokomkar.mediaplay.ui.di

import android.content.Context
import com.alokomkar.mediaplay.ui.main.list.di.ListScope
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import dagger.Module
import dagger.Provides

@Module
class PlayerModule {

    @Provides
    @ListScope
    fun providesTrackSelector() : TrackSelector = DefaultTrackSelector()

    @Provides
    @ListScope
    fun providesLoadControl() : LoadControl = DefaultLoadControl()

    @Provides
    @ListScope
    fun exoPlayer(context: Context, trackSelector: TrackSelector, loadControl : LoadControl ): SimpleExoPlayer
            = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl)

}