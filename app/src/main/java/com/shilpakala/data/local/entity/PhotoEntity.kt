package com.shilpakala.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imagePath: String,
    val productName: String,
    val woodType: String,
    val price: String,
    val timestamp: Long = System.currentTimeMillis()
)