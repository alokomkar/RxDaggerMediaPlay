package com.alokomkar.mediaplay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import com.alokomkar.mediaplay.ui.main.details.DetailsFragment
import com.alokomkar.mediaplay.ui.main.list.ListFragment
import com.alokomkar.mediaplay.ui.main.list.MediaInfoListAdapter

class MainActivity : AppCompatActivity(), MediaInfoListAdapter.ItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            val listFragment = supportFragmentManager.findFragmentByTag("ListScreen") ?: ListFragment()
            supportFragmentManager.beginTransaction().apply {
                addToBackStack("ListScreen")
            }
                .replace(R.id.container, listFragment)
                .commit()
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

    override fun onBackPressed() {
        if( supportFragmentManager.backStackEntryCount > 1 ) {
            supportFragmentManager.popBackStack()
        }
        else
            super.onBackPressed()
    }

}
