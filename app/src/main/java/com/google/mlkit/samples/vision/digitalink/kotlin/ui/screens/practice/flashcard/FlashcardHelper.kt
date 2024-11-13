package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard

import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard

fun updateFlashcardLeitner(
    flashcard: Flashcard,
    isCorrect: Boolean,
    updateAction: (Flashcard) -> Unit
) {


       if (isCorrect) {
           // Move to the next box level (up to the max level of 5)
           flashcard.boxLevel = (flashcard.boxLevel + 1).coerceAtMost(5)
       } else {
           // Move the card back to the first box if the answer is incorrect
           flashcard.boxLevel = 1
       }

       // Calculate the due date based on box level
       val intervalDays = when (flashcard.boxLevel) {
           1 -> 1
           2 -> 3
           3 -> 7
           4 -> 14
           else -> 30
       }
       flashcard.dueDate = System.currentTimeMillis() + intervalDays * 24 * 60 * 60 * 1000L

        updateAction(flashcard)





}