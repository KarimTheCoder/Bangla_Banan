package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.model.FlashcardSessionItem

@Composable
fun ToggleSegmentedButton(navController: NavController, flashcardVM: FlashcardViewModel) {
    // Boolean state for visibility
    // Boolean state for visibility
    val sessionItems by flashcardVM.flashcardSessionItems.observeAsState()

    val viewModel: SessionCompleteViewModel = viewModel()
    val isLessonVisible by viewModel.isLessonVisible.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {


        // Animate appearance and disappearance of TitleListDemo sliding from the right
        AnimatedVisibility(
            visible = isLessonVisible,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),  // Slide in from the right
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()   // Slide out to the right
        ) {
            //DemoNormalList(navController, viewModel)
        }

        // Animate appearance and disappearance of ScrollableListWithFixedButtons sliding from the right
        AnimatedVisibility(
            visible = !isLessonVisible,
            enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),  // Slide in from the right
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()   // Slide out to the right
        ) {


            ResultUI(
                sessionItems,
                onButton1Click = { /* Do something */ },
                onButton2Click = { /* Do something */ }
            )
        }
    }
}

@Composable
fun FlashcardSessionItemView(
    sessionItem: FlashcardSessionItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Display the flashcard word
            Text(
                text = "Word: ${sessionItem.flashcard.word}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Display if this flashcard is the current one
            Text(
                text = if (sessionItem.isCorrect == true) "Currently Reviewing" else "Not Current",
                color = if (sessionItem.isCorrect == true) Color.Blue else Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Display correctness status
            sessionItem.isCorrect?.let {
                Text(
                    text = "Correct: ${if (it) "Yes" else "No"}",
                    color = if (it) Color.Green else Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            } ?: Text(
                text = "Not yet reviewed",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}