package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.model

import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard

data class FlashcardSessionItem(
    val flashcard: Flashcard, // The flashcard being reviewed
    var isCorrect: Boolean? = null, // Track if the answer was correct (null if not yet answered)
    var timestamp: Long? = null // Timestamp for when the flashcard was reviewed
)

data class FlashcardSession(
    val sessionId: Long = System.currentTimeMillis(), // Unique session ID
    val flashcards: List<FlashcardSessionItem>, // List of session items
    var currentIndex: Int = 0, // Tracks the current flashcard in the session
    var startTime: Long = System.currentTimeMillis(), // Start time of session
    var endTime: Long? = null // End time, set when session completes
) {
    // Helper to get the current flashcard item
    fun currentFlashcardItem(): FlashcardSessionItem? =
        flashcards.getOrNull(currentIndex)

    // Move to the next flashcard in the session
    fun nextFlashcard(): FlashcardSessionItem? {
        if (currentIndex < flashcards.size - 1) {
            currentIndex++
            return flashcards[currentIndex]
        }
        return null // No more flashcards
    }

    // Navigate to the previous flashcard
    fun previousFlashcard(): FlashcardSessionItem? {
        if (currentIndex > 0) {
            currentIndex--
            return flashcards[currentIndex]
        }
        return null // Already at the beginning
    }

    // Mark the current flashcard item with a result
    fun markCurrentFlashcard(isCorrect: Boolean) {
        currentFlashcardItem()?.let {
            it.isCorrect = isCorrect
            it.timestamp = System.currentTimeMillis()
        }
    }

    // End the session
    fun endSession() {
        endTime = System.currentTimeMillis()
    }




    // Get session summary
    fun getSummary(): Pair<Int, Int> {
        val correctCount = flashcards.count { it.isCorrect == true }
        val incorrectCount = flashcards.count { it.isCorrect == false }
        return Pair(correctCount, incorrectCount)
    }
}