package com.yourphotos.getImages.repositiory

import android.content.Context
import android.util.Log
import androidx.room.Database
import com.yourphotos.getImages.database.AppDatabase
import com.yourphotos.getImages.sharedStorage.ImageRepository
import com.yourphotos.getImages.sharedStorage.ImagesFromLocalStorage
import com.yourphotos.getImages.sharedStorage.MainImagesFromLocalStorage
import com.yourphotos.model.ImageData
import com.yourphotos.model.ShowImage
import com.yourphotos.utils.HelperFunction
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class MediaRepository {
    private val imageRepository = ImageRepository()
    private val list = ArrayList<ShowImage>()
    private val imageDataList = ArrayList<ImageData>()
    private var lastDate: String? = null

    suspend fun getImageFromSharedStorage(context: Context){
        val dao = AppDatabase.getDatabase(context).getDao()
        imageRepository.storeImageInLocalDB(context).collectLatest {
            dao.clearImageData()
            dao.insertImageData(it)
        }
    }

    suspend fun getImagesWithDate(context: Context) = flow {
        val dao = AppDatabase.getDatabase(context).getDao()
        dao.getImageDataList().collect {
            list.clear()
            if (it.isNotEmpty()) {
                try {
                    var i = 0
                    while(i<it.size){
                        val date = HelperFunction.epochToIso8601(it[i].dateAdded.toLong())
                        if(lastDate==date){
                            list.add(ShowImage(date,it[i]))
                            i++
                        }else{
                            lastDate = date
                            list.add(ShowImage(date,null))
                        }
                    }
                } catch (e: Exception) {

                }
            }
            emit(list)
        }
    }

    fun getImagesFromLocalDB(context: Context) = flow{
        val dao = AppDatabase.getDatabase(context).getDao()
        dao.getImageDataList().collect{
            imageDataList.clear()
            imageDataList.addAll(it)
            emit(imageDataList)
        }
    }


}