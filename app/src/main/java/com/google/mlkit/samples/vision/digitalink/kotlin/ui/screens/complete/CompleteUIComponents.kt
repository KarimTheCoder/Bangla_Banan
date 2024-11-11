package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel

@Composable
fun ToggleSegmentedButton(navController: NavController, flashcardVM: FlashcardViewModel) {
    // Boolean state for visibility
    // Boolean state for visibility
    val sessionItems = remember { flashcardVM.getAllFlashcardSessionItems() }

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