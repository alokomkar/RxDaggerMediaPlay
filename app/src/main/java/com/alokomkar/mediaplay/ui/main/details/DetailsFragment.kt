package com.alokomkar.mediaplay.ui.main.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alokomkar.mediaplay.R
import com.alokomkar.mediaplay.commons.ComponentHolder
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

class DetailsFragment : Fragment() {

    private val component by lazy { ComponentHolder.detailsComponent() }
    @Inject
    lateinit var picasso: Picasso

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        with(arguments?.getParcelable(SELECTED_ITEM) ?: MediaInfo()) {
            picasso
                .load(coverImage)
                .into(ivMediaArt)
            tvName.text = song
            tvDescription.text = artists
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ComponentHolder.destroyDetailsComponent()
    }

    companion object {
        const val SELECTED_ITEM = "selectedItem"
    }
}