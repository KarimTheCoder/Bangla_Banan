package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel

// Navigation buttons to switch flashcards


@Composable
fun BottomTestActions(navController: NavController, flashcardVM: FlashcardViewModel) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {

        Button(onClick = { flashcardVM.previousFlashcard() }, enabled = true) {
            Text("<")
        }
        Button(onClick = { flashcardVM.nextFlashcard() }, enabled = true) {
            Text(">")
        }
        Button(onClick = {
            flashcardVM.endSession()

            navController.navigate("complete_screen") }, enabled = true) {
            Text("End")
        }
    }

}
