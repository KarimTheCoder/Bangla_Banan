package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: Flashcard)

    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)

    @Delete
    suspend fun deleteFlashcard(flashcard: Flashcard)

    @Query("SELECT * FROM flashcard_table WHERE nextReviewDate <= :currentTime")
    suspend fun getDueFlashcards(currentTime: Long): List<Flashcard>

    @Query("SELECT * FROM flashcard_table WHERE word = :word LIMIT 1")
    suspend fun getFlashcard(word: String): Flashcard?

    // New function to get all flashcards
    @Query("SELECT * FROM flashcard_table")
    suspend fun getAllFlashcards(): List<Flashcard>
}

