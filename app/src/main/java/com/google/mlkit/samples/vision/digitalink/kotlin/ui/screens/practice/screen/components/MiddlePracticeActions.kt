package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen.components

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.StrokeManager
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.PracticeUIViewModel

@Composable
fun HorizontalLayoutWithTextButtonAndMatchButton(
    strokeManager: StrokeManager,
    viewModel: PracticeUIViewModel,
    flashcardVM: FlashcardViewModel
) {
    val drawingViewRef = viewModel.drawingViewRef

    val text by viewModel.text.observeAsState("")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically // Aligns the items vertically centered
    ) {
        // TextButton at the start
        TextButton(
            onClick = {  strokeManager.reset()
                drawingViewRef.value?.clear()
                viewModel.updateText("")
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text("Clear")
        }

        // Spacer with weight to push the Text to the center
        Spacer(modifier = Modifier.weight(1f))

        // Centered Text
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        // Spacer with weight to push the Match Button to the end
        Spacer(modifier = Modifier.weight(1f))

        // Match Button at the end
        Button(
            onClick = {


                viewModel.recognizeClick().addOnSuccessListener{ recognizedText->

                    if (recognizedText != null) {

                        Log.i("Recognize stuff","text: $recognizedText")

                        if (flashcardVM.isWrittenWordCorrect(recognizedText)) {

                            viewModel.toggleCorrect()




                        } else {
                            viewModel.toggleWrong()


                        }


                        //Todo: Use more graceful way to remove previous text from canvas
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            viewModel.strokeManager.reset()
//                            viewModel.drawingViewRef.value?.clear()
//                            viewModel.updateText("")
//
//                        }, 1000) // 2000 milliseconds (2 seconds)


                    }


                }



            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text("Match")
        }
    }
}
