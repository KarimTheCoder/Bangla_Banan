package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen.components

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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

    val statusText by viewModel.statusText.observeAsState("")
    val status by viewModel.status.collectAsState()
    Log.i("PracticeUIViewModel",status)

    val isLoading by viewModel.isLoading.collectAsState()
    Log.i("PracticeUIViewModel","isLoading: $isLoading")


    LaunchedEffect(isLoading) {
        if (isLoading) {
            viewModel.startLoadingMessages()
        } else {
            viewModel.stopLoadingMessages()
        }
    }


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
                viewModel.updateStatusText("Ready!")
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text("Clear")
        }

        // Spacer with weight to push the Text to the center
        Spacer(modifier = Modifier.weight(1f))


        // Centered Text
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium
                )

            } else {


                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
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