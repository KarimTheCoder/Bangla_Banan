package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.PracticeTopBar
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.MyXmlButtonView
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.StrokeManager
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.PracticeViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.TwoBoxesWithLines0

@Composable
fun PracticeScreen(navController: NavController, flashcardVM: FlashcardViewModel) {



    val practiceVM = PracticeViewModel()
    practiceVM.initializeDrawingRecognition()
    val strokeManager = practiceVM.strokeManager
    strokeManager.setClearCurrentInkAfterRecognition(true)
    strokeManager.setTriggerRecognitionAfterInput(false)




    val listener = object : StrokeManager.StatusChangedListener {
        override fun onStatusChanged() {



            strokeManager.text?.let {
                flashcardVM.updateWrittenText(it)
                practiceVM.updateText(it)

            }
            Log.i("regtag", "status changed: ${strokeManager.text}")
        }
    }
    strokeManager.setStatusChangedListener(listener)

    Column {

        PracticeTopBar(navController)
        TwoBoxesWithLines0(practiceVM, flashcardVM)
        HorizontalLayoutWithTextButtonAndMatchButton(strokeManager, practiceVM,flashcardVM)
        MyXmlButtonView(strokeManager,practiceVM)


        // Navigation buttons to switch flashcards
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {

            Button(onClick = { flashcardVM.previousFlashcard() }, enabled = true) {
                Text("<")
            }
            Button(onClick = { flashcardVM.nextFlashcard() }, enabled = true) {
                Text(">")
            }
            Button(onClick = { flashcardVM.endSession()
                navController.navigate("complete_screen")
                             }, enabled = true) {
                Text("End")
            }
        }


    }

}