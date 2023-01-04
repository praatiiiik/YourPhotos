package com.yourphotos.getImages.sharedStorage

import android.content.Context
import android.util.Log
import com.yourphotos.model.ImageData
import com.yourphotos.model.ShowImage
import com.yourphotos.utils.HelperFunction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class ImageRepository {
    private val imagesFromLocalStorage: MainImagesFromLocalStorage = ImagesFromLocalStorage()

    suspend fun storeImageInLocalDB(context: Context) : Flow<List<ImageData>> {
        return imagesFromLocalStorage.getImagesFromStorage(context)
    }
}