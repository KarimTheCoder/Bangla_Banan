package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.model

import android.util.Log
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard

    data class FlashcardSessionItem(
    val flashcard: Flashcard, // The flashcard being reviewed
    var isCorrect: Boolean? = null, // Track if the answer was correct (null if not yet answered)
    var timestamp: Long? = null, // Timestamp for when the flashcard was reviewed
    var isBoxChangedThisSession: Boolean = false, // Prevent multiple box changes per session
    var reviewCount: Int = 0, // Track the number of reviews for this flashcard
    var maxReviewCount: Int = if (flashcard.isFamiliarized()) 1 else flashcard.getUnfamiliarMaxReviewCount() // Default max reviews based on familiarity
)

data class FlashcardSession(
    val sessionId: Long = System.currentTimeMillis(), // Unique session ID
    val flashcards: List<FlashcardSessionItem>, // List of session items
    var currentIndex: Int = 0, // Tracks the current flashcard in the session
    var startTime: Long = System.currentTimeMillis(), // Start time of session
    var endTime: Long? = null,
    val updateAction: (Flashcard) -> Unit, // Action to update flashcards
    val updateFlashcardLeitner: (Flashcard,Boolean) -> Unit,
) {


    private val TAG = "FlashcardSession"

    // Helper to get the current flashcard item
    fun currentFlashcardItem(): FlashcardSessionItem? =
        flashcards.getOrNull(currentIndex)

    // Mark the current flashcard item as reviewed
    fun markCurrentFlashcard(isCorrect: Boolean) {

        Log.i(TAG, "Flashcard sizeg ${flashcards.size}")
        currentFlashcardItem()?.let { item ->
            if (item.reviewCount < item.maxReviewCount) {
                item.isCorrect = isCorrect
                item.timestamp = System.currentTimeMillis()
                item.reviewCount++ // Increment the review count

                Log.i(TAG, "MaxReview count ${item.maxReviewCount}")
                Log.i(TAG, "Marked flashcard '${item.flashcard.word}' as ${if (isCorrect) "correct" else "incorrect"}")
                Log.i(TAG, "Review count for '${item.flashcard.word}' is now ${item.reviewCount}")






                // Adjust max review count based on familiarity and correctness
                if (!isCorrect) {
                    if (item.flashcard.isFamiliarized()) {
                        // For familiarized flashcards, maxReviewCount can increase up to 2
                        if (item.maxReviewCount < 2) {
                            item.maxReviewCount++
                            Log.i(TAG, "Increased max review count for familiarized flashcard '${item.flashcard.word}' to ${item.maxReviewCount}")
                        }

                    } else {
                        // For not familiarized flashcards, maxReviewCount can increase up to 5
                        if (item.maxReviewCount < 5) {
                            item.maxReviewCount++
                            Log.i(TAG, "Increased max review count for not familiarized flashcard '${item.flashcard.word}' to ${item.maxReviewCount}")
                        }
                    }
                }else{
                //Todo: if correct update the flashcard database

                    if (item.flashcard.isFamiliarized()) {

                        updateFlashcardLeitner(item.flashcard,isCorrect)

                    } else {
                        item.flashcard.familiarityCount++
                        updateAction(item.flashcard)



                    }
                }

                updateAction(item.flashcard) // Trigger the update action
            } else {
                Log.i(TAG, "Flashcard '${item.flashcard.word}' has already been reviewed ${item.maxReviewCount} times")
            }
        }
    }

    // Move to the next flashcard
    fun nextFlashcard(): FlashcardSessionItem? {
        if (isSessionComplete()) {
            Log.i(TAG, "Session is complete. No more flashcards to review.")
            return null // Stop navigation if the session is complete
        }

        var attempts = 0
        do {
            currentIndex = (currentIndex + 1) % flashcards.size
            attempts++
            Log.i(TAG, "Navigating to next flashcard. Current index: $currentIndex")
        } while (flashcards[currentIndex].reviewCount >= flashcards[currentIndex].maxReviewCount && attempts < flashcards.size)

        return flashcards[currentIndex].takeIf {
            it.reviewCount < it.maxReviewCount
        }?.also {
            Log.i(TAG, "Next flashcard to review: '${it.flashcard.word}'")
        }
    }

    // Navigate to the previous flashcard
    fun previousFlashcard(): FlashcardSessionItem? {
        if (isSessionComplete()) {
            Log.i(TAG, "Session is complete. No more flashcards to review.")
            return null // Stop navigation if the session is complete
        }

        var attempts = 0
        do {
            currentIndex = if (currentIndex > 0) currentIndex - 1 else flashcards.size - 1
            attempts++
            Log.i(TAG, "Navigating to previous flashcard. Current index: $currentIndex")
        } while (flashcards[currentIndex].reviewCount >= flashcards[currentIndex].maxReviewCount && attempts < flashcards.size)

        return flashcards[currentIndex].takeIf {
            it.reviewCount < it.maxReviewCount
        }?.also {
            Log.i(TAG, "Previous flashcard to review: '${it.flashcard.word}'")
        }
    }

    // End the session
    fun endSession() {
        endTime = System.currentTimeMillis()
        Log.i(TAG, "Session ended at $endTime")
    }

    // Check if session is complete (all flashcards reviewed up to their respective maxReviewCount)
    fun isSessionComplete(): Boolean {
        val isComplete = flashcards.all { it.reviewCount >= it.maxReviewCount }
        Log.i(TAG, "Session complete: $isComplete")
        return isComplete
    }
}