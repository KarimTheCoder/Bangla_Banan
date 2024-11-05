package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlashcardRepository @Inject constructor(
    private val flashcardDao: FlashcardDao
) {

    suspend fun insertFlashcard(flashcard: Flashcard) {
        flashcardDao.insertFlashcard(flashcard)
    }

    suspend fun updateFlashcard(flashcard: Flashcard) {
        flashcardDao.updateFlashcard(flashcard)
    }

    suspend fun deleteFlashcard(flashcard: Flashcard) {
        flashcardDao.deleteFlashcard(flashcard)
    }

    suspend fun getDueFlashcards(currentTime: Long): List<Flashcard> {
        return flashcardDao.getDueFlashcards(currentTime)
    }

    suspend fun getFlashcard(word: String): Flashcard? {
        return flashcardDao.getFlashcard(word)
    }

    suspend fun getAllFlashcards():List<Flashcard>{

        return flashcardDao.getAllFlashcards()
    }
}