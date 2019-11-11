package com.alokomkar.mediaplay.ui.main.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alokomkar.core.extensions.handleFailures
import com.alokomkar.core.networking.Response
import com.alokomkar.mediaplay.MediaPlayerControls
import com.alokomkar.mediaplay.R
import com.alokomkar.mediaplay.commons.ComponentHolder
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import com.alokomkar.mediaplay.ui.main.list.viewmodel.ListViewModel
import com.alokomkar.mediaplay.ui.main.list.viewmodel.ListViewModelFactory
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject


class ListFragment : Fragment(), MediaInfoListAdapter.ItemClickListener {

    private val component by lazy { ComponentHolder.listComponent() }
    @Inject
    lateinit var viewModelFactory: ListViewModelFactory
    @Inject
    lateinit var adapter: MediaInfoListAdapter

    private lateinit var onItemSelection: MediaInfoListAdapter.ItemClickListener
    private lateinit var mediaPlayerControls: MediaPlayerControls

    private val viewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private var selectedItem : MediaInfo ?= null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if( context is MediaInfoListAdapter.ItemClickListener ) {
            onItemSelection = context
        }
        if( context is MediaPlayerControls ) {
            mediaPlayerControls = context
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
        scheduleRefreshLayout.isRefreshing = true
        scheduleRefreshLayout.setOnRefreshListener { viewModel.getMedia() }

        initialiseDataListener()

        fabPlayPause.isEnabled = false
        fabPlayPause.alpha = 0.5f

        fabPlayPause.setOnClickListener {
            togglePlayPause()
        }

    }

    private fun togglePlayPause() {

        if( selectedItem == null ) {
            selectedItem = adapter.getItemAtPosition(0)
            selectedItem?.let { onItemClicked(it) }
            fabPlayPause.setImageDrawable(ContextCompat.getDrawable(fabPlayPause.context, android.R.drawable.ic_media_pause))
            return
        }
        else {
            selectedItem?.let { item ->
                if( !mediaPlayerControls.isPlaying() ) {
                    fabPlayPause.setImageDrawable(ContextCompat.getDrawable(fabPlayPause.context, android.R.drawable.ic_media_pause))
                    mediaPlayerControls.playMedia(item)
                }
                else {
                    fabPlayPause.setImageDrawable(ContextCompat.getDrawable(fabPlayPause.context, android.R.drawable.ic_media_play))
                    mediaPlayerControls.pauseMedia()
                }
            }

        }
    }

    private fun showToast(message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initialiseDataListener() {
        viewModel.mediaListResponse.observe(this, Observer { response ->

            fabPlayPause.alpha = if( response is Response.Success ) 1.0f else 0.5f
            fabPlayPause.isEnabled = response is Response.Success

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



    override fun onItemClicked(item: MediaInfo) {
        selectedItem = item
        showToast(R.string.loading)
        mediaPlayerControls.playMedia(item)

        //Not working as response is required in JSON Format
        //viewModel.resolveShortUrl(item.url)
        //onItemSelection.onItemClicked(item)
    }

}