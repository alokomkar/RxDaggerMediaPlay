package com.alokomkar.mediaplay.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.webkit.WebView
import com.alokomkar.mediaplay.BuildConfig
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class MediaPlayService : Service() {

    private val dataSourceFactory by lazy { DefaultDataSourceFactory(this, "${BuildConfig.APPLICATION_ID}.AudioPlayer") }
    private val extractorsFactory : ExtractorsFactory by lazy { DefaultExtractorsFactory() }
    private val webView by lazy { WebView(this) }

    private val iBinder : IBinder by lazy {
        MediaPlayBinder()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return iBinder
    }

    override fun onCreate() {
        super.onCreate()

    }
}