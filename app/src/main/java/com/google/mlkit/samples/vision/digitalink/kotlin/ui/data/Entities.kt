package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_table")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    var boxLevel: Int = 0, // Start at box level 0
    var nextReviewDate: Long = System.currentTimeMillis() // Start immediately
)