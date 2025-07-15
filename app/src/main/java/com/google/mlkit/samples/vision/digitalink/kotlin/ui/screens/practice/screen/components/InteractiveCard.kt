package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen.components

import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.lottie.CorrectAnimation
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.lottie.WrongAnimation
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.PracticeUIViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.CardState
import com.samsulkarim.bananapp.R
import java.util.Locale

@Composable
fun TwoBoxesWithLines0(viewModel: PracticeUIViewModel, cardViewModel: FlashcardViewModel) {
    val currentFlashcard by cardViewModel.currentFlashcard.observeAsState()
    val currentWord by cardViewModel.currentWord.observeAsState()
    val writtenText by cardViewModel.writtenText.observeAsState()

    val isVisible by viewModel.isVisible.collectAsState()

    val cardState by viewModel.cardState.observeAsState(CardState.SHOW)



    val scale by animateFloatAsState(targetValue = if (isVisible) 1f else 0f, label = "")

    val backgroundColor by viewModel.backgroundColor.collectAsState(initial = Color.LightGray)

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(150.dp),
        contentAlignment = Alignment.Center
    ) {

        if(!isVisible){

            if(cardState == CardState.CORRECT){

                CorrectAnimation()

            }else{

                WrongAnimation()
            }
            

        }
        if(cardState == CardState.CORRECT){


            SlideInWordCard(scale = scale, isVisible = isVisible, currentFlashcard,cardViewModel)
        }else{


            WordCard(scale = scale, currentFlashcard,cardViewModel)



            LaunchedEffect(writtenText, currentWord) {
                Toast.makeText(context, "Written: $writtenText Correct: $currentWord", Toast.LENGTH_LONG).show()
            }     }


    }
}

@Composable
fun SlideInWordCard(
    scale: Float,
    isVisible: Boolean,
    currentFlashcard: Flashcard?,
    cardViewModel: FlashcardViewModel, ) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { it }, // `it` refers to the width of the composable
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        WordCard(scale, currentFlashcard, cardViewModel)
    }
}


@Composable
private fun WordCard(scale: Float, currentFlashcard: Flashcard?, cardViewModel: FlashcardViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {


            // First Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
                    .fillMaxHeight()
                    .graphicsLayer(scaleX = scale, scaleY = scale), // Scale the first box in place
                contentAlignment = Alignment.Center
            ) {


                AudioIconButton(currentFlashcard,cardViewModel )
            }




            // Vertical divider line
            HorizontalDivider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    ), // Scale the vertical divider in place
                color = Color.Black
            )

            // Second Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale), // Scale the second box in place
                contentAlignment = Alignment.Center
            ) {
                RevealTextButton(currentFlashcard) // Your custom button
            }
        }
    }
}

@Composable
fun AudioIconButton(currentFlashcard: Flashcard?, cardViewModel: FlashcardViewModel) {


    val onIconClick by cardViewModel.onIconClick.collectAsState()
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

    // Trigger the delay within a LaunchedEffect when showAnimation changes to true
    LaunchedEffect(onIconClick) {
        if (onIconClick) {

            tts?.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, null)


            showAnimation = true
            kotlinx.coroutines.delay(2000) // Wait for the animation duration
            showAnimation = false
            cardViewModel.resetIconClick()
        }
    }

    // IconButton with logic to toggle between icon and animation
    IconButton(
        onClick = {

            cardViewModel.triggerIconClick()


//            tts?.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, null)
//            showAnimation = true // Start animation when button is clicked
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
                imageVector = ImageVector.vectorResource(id = R.drawable.rounded_lightbulb_24),
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


