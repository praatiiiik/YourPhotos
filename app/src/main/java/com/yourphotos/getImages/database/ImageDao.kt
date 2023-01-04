package com.yourphotos.getImages.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yourphotos.model.ImageData
import com.yourphotos.model.ShowImage
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImageData(list: List<ImageData>)

    @Query("SELECT * FROM imageData order by id DESC")
    fun getImageDataList(): Flow<List<ImageData>>

    @Query("DELETE FROM imageData")
    fun clearImageData()
}
