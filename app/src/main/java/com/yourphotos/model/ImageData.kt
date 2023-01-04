package com.yourphotos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "imageData")
data class ImageData(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val contentUri: String,
    val dateAdded:String
)

