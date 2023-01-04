package com.yourphotos.getImages.sharedStorage

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.yourphotos.model.ImageData
import com.yourphotos.utils.sdk29AndUp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

interface MainImagesFromLocalStorage {
    suspend fun getImagesFromStorage(context: Context): Flow<List<ImageData>>
}

class ImagesFromLocalStorage : MainImagesFromLocalStorage {

    override suspend fun getImagesFromStorage(context: Context): Flow<List<ImageData>> = flow {
        try{
            val collection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI


            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.DATE_ADDED
            )
            val photos = mutableListOf<ImageData>()
            context.contentResolver.query(
                collection,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
                val dateAdded = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)
                    val date = cursor.getString(dateAdded) ?: "0"
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    photos.add(ImageData(id, displayName, width, height, contentUri.toString(), date))
                }
                Log.d("imageData", photos.size.toString())
            }
            emit(photos)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}