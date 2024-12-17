package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.mlkit.samples.vision.digitalink.R
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.StrokeManager
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.PracticeViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.model.FlashcardSessionItem
import kotlinx.coroutines.delay
import java.util.Locale


@Composable
fun AudioIconButton(currentFlashcard: Flashcard?) {

    //todo: null check
    val currentWord = currentFlashcard?.word

    // Controls whether to show the animation or the icon
    var showAnimation by remember { mutableStateOf(false) }

    // Lottie composition for the animation
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sound))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = showAnimation
    )

    // Trigger the delay within a LaunchedEffect when showAnimation changes to true
    LaunchedEffect(showAnimation) {
        if (showAnimation) {
            kotlinx.coroutines.delay(2000) // Show animation for 2 seconds
            showAnimation = false
        }
    }

    val context = LocalContext.current
    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    DisposableEffect(context) {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Set engine to Google TTS
                val googleTtsEngine = "com.google.android.tts"

                try {
                    // Force using Google TTS engine
                    tts?.setEngineByPackageName(googleTtsEngine)
                    Log.d("TTS", "Using Google TTS engine.")
                } catch (e: Exception) {
                    Log.e(
                        "TTS",
                        "Google TTS engine is not available. Falling back to default engine."
                    )
                    Toast.makeText(
                        context,
                        "Google TTS engine not available. Using default engine.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                // Check if Bengali language is available
                val langStatus = tts?.isLanguageAvailable(Locale("bn"))
                if (langStatus == TextToSpeech.LANG_AVAILABLE) {
                    tts?.language = Locale("bn")
                    Log.d("TTS", "Bengali language is available.")
                } else {
                    Log.e("TTS", "Bengali language is not available, falling back to English.")
                    tts?.language = Locale("en") // Fallback to English if Bengali is unavailable
                }
            } else {
                Log.e("TTS", "TextToSpeech initialization failed.")
            }
        })

        onDispose {
            // Shutdown TTS when composable is disposed
            tts?.shutdown()
        }
    }



    // IconButton with logic to toggle between icon and animation
    IconButton(
        onClick = {

         


            tts?.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, null)
            showAnimation = true // Start animation when button is clicked
        },
        modifier = Modifier.fillMaxSize()
    ) {
        if (showAnimation) {
            // Display Lottie animation
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(64.dp)
            )
        } else {
            // Display icon
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.outline_hearing_24),
                contentDescription = "Favorite icon",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}





@Composable
fun RevealTextButton(currentFlashcard: Flashcard?) {
    // State to track whether the text is visible
    var isTextVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(animationSpec = tween(durationMillis = 300)), // Smooth animation
        horizontalAlignment = Alignment.CenterHorizontally // Centers the content horizontally
    ) {
        // IconButton
        IconButton(onClick = { isTextVisible = !isTextVisible }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.outline_lock_24),
                tint = Color.Black,
                contentDescription = "Reveal text"
            )
        }

        // Conditionally display the text when isTextVisible is true
        if (isTextVisible) {
            Text(
                text = currentFlashcard!!.word,
                modifier = Modifier.padding(top = 8.dp), // Adds some padding at the top
                style = MaterialTheme.typography.bodyMedium // Apply any text style
            )
        }
    }
}
@Composable
fun HorizontalLayoutWithTextButtonAndMatchButton(
    strokeManager: StrokeManager,
    viewModel: PracticeViewModel,
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

                        Log.i("Recogize stuff","text: $recognizedText")

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