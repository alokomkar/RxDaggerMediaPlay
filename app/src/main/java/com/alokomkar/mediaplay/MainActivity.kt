package com.alokomkar.mediaplay

import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.alokomkar.mediaplay.commons.ComponentHolder
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import com.alokomkar.mediaplay.ui.main.details.DetailsFragment
import com.alokomkar.mediaplay.ui.main.list.ListFragment
import com.alokomkar.mediaplay.ui.main.list.MediaInfoListAdapter
import com.alokomkar.mediaplay.ui.viewmodel.MainViewModel
import com.alokomkar.mediaplay.ui.viewmodel.MainViewModelFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MediaInfoListAdapter.ItemClickListener, MediaPlayerControls {

    private val component by lazy { ComponentHolder.mainActivityComponent() }

    @Inject
    lateinit var exoPlayer: SimpleExoPlayer

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private val dataSourceFactory by lazy { DefaultDataSourceFactory(this, "${BuildConfig.APPLICATION_ID}.AudioPlayer") }
    private val extractorsFactory : ExtractorsFactory by lazy { DefaultExtractorsFactory() }
    private val webView by lazy { WebView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        component.inject(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, ListFragment())
                addToBackStack("ListScreen")
            }.commit()
        }
    }

    override fun onItemClicked(item: MediaInfo) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DetailsFragment.SELECTED_ITEM, item)
                }
            })
            addToBackStack("DetailsScreen")
        }.commit()
    }

    override fun playMedia(mediaInfo: MediaInfo) {
        webView.loadUrl(mediaInfo.url)
        fabPlayPause.setImageDrawable(ContextCompat.getDrawable(fabPlayPause.context, android.R.drawable.ic_media_pause))

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                mediaInfo.expandedUrl = webView.url
                viewModel.updateMedia(mediaInfo)
                playMedia(webView.url)
            }
        }
    }

    override fun pauseMedia() {
        exoPlayer.playWhenReady = false
        exoPlayer.playbackState
    }

    override fun resumeMedia() {
        exoPlayer.playWhenReady = true
        exoPlayer.playbackState
    }

    override fun stopMedia() {
        exoPlayer.playWhenReady = false
        exoPlayer.playbackState
    }

    override fun isPlaying(): Boolean {
        return exoPlayer.playWhenReady
    }

    override fun onBackPressed() {
        finish()
    }

    private fun playMedia(data: String) {
        exoPlayer.release()
        exoPlayer.apply {
            prepare(
                ExtractorMediaSource(
                    Uri.parse(data),
                    dataSourceFactory,
                    extractorsFactory,
                    null,
                    null)
            )
            playWhenReady = !playWhenReady
        }
    }

}
