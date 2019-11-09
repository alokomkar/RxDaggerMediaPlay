package com.alokomkar.mediaplay.commons.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alokomkar.mediaplay.ui.main.data.MediaInfo

@Database( entities = [MediaInfo::class], version = 1, exportSchema = false)
abstract class MediaDb : RoomDatabase() {
    abstract fun mediaInfoDao(): MediaInfoDao
}