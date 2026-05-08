package com.shilpakala.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shilpakala.data.local.dao.PhotoDao
import com.shilpakala.data.local.entity.PhotoEntity

@Database(
    entities = [PhotoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ShilpaKalaDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {
        @Volatile
        private var INSTANCE: ShilpaKalaDatabase? = null

        fun getDatabase(context: Context): ShilpaKalaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShilpaKalaDatabase::class.java,
                    "shilpakala_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}