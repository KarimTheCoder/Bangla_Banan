package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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


        PartialBottomSheet(flashcardVM)


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialBottomSheet(flashcardVM: FlashcardViewModel) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { showBottomSheet = true }
        ) {
            Text("Display partial bottom sheet")

        }

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                Column {

                    //todo null check
                    flashcardVM.fetchFamiliarityProgress(flashcardVM.currentFlashcard.value!!.flashcardId)
                    val familiarityProgress by flashcardVM.familiarityProgress.collectAsState()

                    flashcardVM.fetchSpacedRepetitionProgress(flashcardVM.currentFlashcard.value!!.flashcardId)
                    val spacedRepetitionProgress by flashcardVM.spacedRepetitionProgress.collectAsState()

                    VerticalCheckboxWithLabelsAndLines(familiarityProgress)
                    VerticalCheckboxWithLabelsAndLines(spacedRepetitionProgress)
                }

            }
        }
    }
}
@Composable
fun VerticalCheckboxWithLabelsAndLines(checkboxStates: List<Boolean>) {
    // State holders for checkboxes
    //val checkBoxStates = remember { mutableStateListOf(true, true, true, false, false) }
    val labels = listOf("Step 1", "Step 2", "Step 3", "Step 4", "Final") // Labels for checkboxes

    Column(
        modifier = Modifier
            .width(200.dp)
            .padding(start = 16.dp, bottom = 24.dp)
    // Adjust width to fit checkbox and label
    ) {

        Text(
            text = "Familiarization progress",
            color = Color.Black,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding( bottom = 16.dp) // Add spacing between checkbox and label
        )


        checkboxStates.forEachIndexed { index, isChecked ->
            Column() {
                // Row for checkbox and label
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp) // Spacing between rows
                ) {
                    // Render the checkbox
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = null, // Make the checkbox unclickable
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Blue,
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )
                    )

                    // Render the label next to the checkbox
                    Text(
                        text = labels[index],
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp) // Add spacing between checkbox and label
                    )
                }

                // Add a vertical line between checkboxes except after the last one
                if (index < checkboxStates.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp)) // Add spacing before the line
                    Canvas(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .width(2.dp)
                            .height(20.dp) // Length of the vertical line
                    ) {
                        drawLine(
                            color = if (isChecked) Color.Blue else Color.Gray, // Dynamic line color
                            start = center.copy(y = 0f),
                            end = center.copy(y = size.height),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Add spacing after the line
                }
            }
        }
    }
}