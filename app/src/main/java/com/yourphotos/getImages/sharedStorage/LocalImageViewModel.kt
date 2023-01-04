package com.yourphotos.getImages.sharedStorage

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yourphotos.getImages.repositiory.MediaRepository
import com.yourphotos.model.ImageData
import com.yourphotos.model.ShowImage
import com.yourphotos.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LocalImageViewModel(application: Application) : AndroidViewModel(application) {
    private val _showImage: MutableStateFlow<List<ShowImage>?> = MutableStateFlow(null)
    private val _imageData: MutableStateFlow<List<ImageData>?> = MutableStateFlow(null)
    val showImage: StateFlow<List<ShowImage>?> = _showImage.asStateFlow()
    val imageData: StateFlow<List<ImageData>?> = _imageData.asStateFlow()
    var clickedData = 0L
    private val mediaRepository = MediaRepository()

    fun getLocalImage() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaRepository.getImageFromSharedStorage(getApplication())
            mediaRepository.getImagesWithDate(getApplication())
                .shareIn(this, SharingStarted.WhileSubscribed(5000L), 1)
                .collectLatest {
                    _showImage.value = it
                }
        }
    }

    fun getImageForViewPager(){
        viewModelScope.launch(Dispatchers.IO) {
            mediaRepository.getImagesFromLocalDB(getApplication())
                .shareIn(this, SharingStarted.WhileSubscribed(5000L), 1)
                .collectLatest {
                    _imageData.value = it
                }
        }
    }

    fun getClickedImagePos(pos:Long){
        clickedData = pos
    }
}