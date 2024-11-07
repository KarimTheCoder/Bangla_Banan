package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.flashcard.FlashcardViewModel

@Composable
fun FlashcardViewer(viewModel: FlashcardViewModel) {
    // Observe the current flashcard
    val currentFlashcard by viewModel.currentFlashcard.observeAsState()

    // Total count of flashcards (useful for enabling/disabling buttons)
    val flashcardsCount = viewModel.flashcards.observeAsState(emptyList()).value.size

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (currentFlashcard != null) {
            // Display the flashcard's content
            Text(
                text = currentFlashcard!!.word,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = currentFlashcard!!.definition,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Text(
                text = "No flashcards available",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Navigation buttons to switch flashcards
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { viewModel.previousFlashcard() }, enabled = flashcardsCount > 1) {
                Text("Previous")
            }
            Button(onClick = { viewModel.nextFlashcard() }, enabled = flashcardsCount > 1) {
                Text("Next")
            }
        }
    }
}