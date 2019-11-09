package com.alokomkar.mediaplay.commons.local

import androidx.room.*
import com.alokomkar.mediaplay.ui.main.data.MediaInfo
import io.reactivex.Flowable

@Dao
interface MediaInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll( mediaList: List<MediaInfo> )

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMedia( mediaInfo: MediaInfo )

    @Delete
    fun delete(MediaInfo: MediaInfo)

    @Query("SELECT * FROM MediaInfo")
    fun getAll(): Flowable<List<MediaInfo>>
    
}