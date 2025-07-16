package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.MyXmlButtonView
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.StrokeManager
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.PracticeUIViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen.components.DeterminateLinearProgress
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen.components.HorizontalLayoutWithTextButtonAndMatchButton
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen.components.PracticeTopBar
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen.components.TwoBoxesWithLines0

@Composable
fun PracticeScreen(
    navController: NavController,
    flashcardVM: FlashcardViewModel
) {
    val practiceVM = PracticeUIViewModel()
    // Initialize the ViewModel logic outside the UI
    LaunchedEffect(Unit) {

        setupPracticeScreen(practiceVM, flashcardVM)
    }

    Column {
        // Organized sections into smaller composables
        PracticeTopBar(navController)
        DeterminateLinearProgress(flashcardVM,navController)
        PracticeContent(practiceVM, flashcardVM)
        //BottomTestActions(navController, flashcardVM)
        //PartialBottomSheet(flashcardVM)
    }
}


fun setupPracticeScreen(
    practiceVM: PracticeUIViewModel,
    flashcardVM: FlashcardViewModel
) {
    practiceVM.initializeDrawingRecognition()
    val strokeManager = practiceVM.strokeManager

    strokeManager.setClearCurrentInkAfterRecognition(true)
    strokeManager.setTriggerRecognitionAfterInput(false)

    val listener = object : StrokeManager.StatusChangedListener {
        override fun onStatusChanged() {
            strokeManager.text?.let {
                flashcardVM.updateStatusText(it)
                practiceVM.updateStatusText(it)
            }
        }
    }
    strokeManager.setStatusChangedListener(listener)
}





@Composable
fun PracticeContent(
    practiceVM: PracticeUIViewModel,
    flashcardVM: FlashcardViewModel
) {
    val strokeManager = practiceVM.strokeManager

    Column {
        TwoBoxesWithLines0(practiceVM, flashcardVM)
        HorizontalLayoutWithTextButtonAndMatchButton(strokeManager, practiceVM, flashcardVM)
        MyXmlButtonView(strokeManager, practiceVM)
    }
}