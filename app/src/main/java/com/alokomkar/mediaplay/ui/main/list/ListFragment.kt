package com.alokomkar.mediaplay.ui.main.list

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alokomkar.core.extensions.handleFailures
import com.alokomkar.core.networking.Response
import com.alokomkar.mediaplay.BuildConfig
import com.alokomkar.mediaplay.R
import com.alokomkar.mediaplay.commons.ComponentHolder
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import com.alokomkar.mediaplay.ui.main.list.viewmodel.ListViewModel
import com.alokomkar.mediaplay.ui.main.list.viewmodel.ListViewModelFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject


class ListFragment : Fragment(), MediaInfoListAdapter.ItemClickListener {

    private val component by lazy { ComponentHolder.listComponent() }
    @Inject
    lateinit var viewModelFactory: ListViewModelFactory
    @Inject
    lateinit var adapter: MediaInfoListAdapter
    @Inject
    lateinit var exoPlayer: SimpleExoPlayer

    private lateinit var onItemSelection: MediaInfoListAdapter.ItemClickListener

    private val viewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private var selectedItem : MediaInfo ?= null

    private val dataSourceFactory by lazy { DefaultDataSourceFactory(context, "${BuildConfig.APPLICATION_ID}.AudioPlayer") }
    private val extractorsFactory : ExtractorsFactory by lazy { DefaultExtractorsFactory() }
    private val webView by lazy { WebView(context) }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if( context is MediaInfoListAdapter.ItemClickListener ) {
            onItemSelection = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        adapter.itemClickListener = this
        rvMedia.adapter = adapter

        viewModel.getMedia()
        scheduleRefreshLayout.setOnRefreshListener { viewModel.getMedia() }

        initialiseDataListener()
        fabPlayPause.setOnClickListener {
            showToast(R.string.to_de_done)
        }

    }

    private fun showToast(message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initialiseDataListener() {
        viewModel.mediaListResponse.observe(this, Observer { response ->
            when( response ) {
                is Response.Progress -> scheduleRefreshLayout.isRefreshing = response.loading
                is Response.Success -> adapter.swapData(response.data)
                is Response.Failure -> handleFailures(response.e) { viewModel.getMedia() }
            }
        })

        //Not working as response is required in JSON Format
        /*viewModel.urlResolverResponse.observe(this, Observer { response ->
            when( response ) {
                is Response.Progress -> showToast(R.string.loading)
                is Response.Success -> playMedia(response.data)
                is Response.Failure -> handleFailures(response.e) { viewModel.resolveShortUrl(selectedItem?.url ?: "") }
            }
        })*/
    }

    private fun playMedia(data: String) {
        Log.d("ListScreen", "play Media : $data")
        exoPlayer.release()
        exoPlayer.apply {
            prepare(ExtractorMediaSource(
                Uri.parse(data),
                dataSourceFactory,
                extractorsFactory,
                null,
                null))
            playWhenReady = true
        }
    }

    @Synchronized
    override fun onItemClicked(item: MediaInfo) {
        selectedItem = item
        if( item.expandedUrl.isEmpty() ) {
            webView.loadUrl(item.url)
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    item.expandedUrl = webView.url
                    playMedia(webView.url)
                }
            }
        }
        else{
            playMedia(item.expandedUrl)
        }

        //Not working as response is required in JSON Format
        //viewModel.resolveShortUrl(item.url)
        //onItemSelection.onItemClicked(item)
    }
}