package com.yourphotos.getImages.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yourphotos.model.ImageData
import com.yourphotos.model.ShowImage

@Database(entities = [ImageData::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): ImageDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "your_photos_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}