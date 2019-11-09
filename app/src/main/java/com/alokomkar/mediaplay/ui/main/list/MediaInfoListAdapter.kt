package com.alokomkar.mediaplay.ui.main.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alokomkar.mediaplay.R
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_media_information.view.*

class MediaInfoListAdapter(private val picasso: Picasso ) : ListAdapter<MediaInfo, MediaInfoListAdapter.ListViewHolder>(LaunchDataDiffComparator()) {

    var itemClickListener : ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder
            = ListViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.item_media_information, parent, false),
        onItemClickListener = itemClickListener)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    fun swapData(data: List<MediaInfo>) {
        submitList(data.toMutableList())
    }

    fun getItemAtPosition(position: Int): MediaInfo {
        return getItem(position)
    }

    interface ItemClickListener {
        fun onItemClicked( item : MediaInfo)
    }

    private class LaunchDataDiffComparator : DiffUtil.ItemCallback<MediaInfo>() {

        override fun areItemsTheSame(oldItem: MediaInfo, newItem: MediaInfo): Boolean
                = oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: MediaInfo, newItem: MediaInfo): Boolean
                = oldItem == newItem

    }

    inner class ListViewHolder(
        itemView : View,
        private val onItemClickListener: ItemClickListener? ) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        override fun onClick(view: View?) {
            onItemClickListener?.onItemClicked(getItem(adapterPosition))
        }

        fun bindItem(item: MediaInfo) = with(itemView) {
            tvTitle.text = item.song
            tvDescription.text = item.artists
            picasso.load(item.coverImage)
                .into(ivMediaArt)
        }

        init {
            itemView.setOnClickListener(this)
        }

    }

}