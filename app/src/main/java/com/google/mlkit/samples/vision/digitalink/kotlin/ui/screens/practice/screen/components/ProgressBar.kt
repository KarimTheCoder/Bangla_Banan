package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel

@Composable
fun DeterminateLinearProgress(flashcardViewModel: FlashcardViewModel) {
    val sessionProgress by flashcardViewModel.sessionProgress.collectAsState()


    LinearProgressIndicator(
        progress = { sessionProgress },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    )
}