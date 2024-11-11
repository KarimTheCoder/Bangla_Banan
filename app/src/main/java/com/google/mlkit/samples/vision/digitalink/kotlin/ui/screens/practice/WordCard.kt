package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.lottie.CorrectAnimation
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.lottie.WrongAnimation
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.Flashcard
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.flashcard.FlashcardViewModel

@Composable
fun TwoBoxesWithLines0(viewModel: PracticeViewModel, cardViewModel: FlashcardViewModel) {
    val currentFlashcard by cardViewModel.currentFlashcard.observeAsState()

    val isVisible by viewModel.isVisible.collectAsState()

    val cardState by viewModel.cardState.observeAsState(CardState.SHOW)



    val scale by animateFloatAsState(targetValue = if (isVisible) 1f else 0f, label = "")

    val backgroundColor by viewModel.backgroundColor.collectAsState(initial = Color.LightGray)


    Box(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(150.dp),
        contentAlignment = Alignment.Center
    ) {




//        when (cardState) {
//            CardState.SHOW -> SlideInWordCard(scale,isVisible)
//
//            CardState.CORRECT -> {
//                CorrectAnimation()
//                SlideInWordCard(scale, isVisible)
//            }
//
//            CardState.WRONG -> {
//                WrongAnimation()
//                WordCard(scale)
//            }
//        }


        if(!isVisible){

            if(cardState == CardState.CORRECT){

                CorrectAnimation()

            }else{

                WrongAnimation()
            }
            

        }
        if(cardState == CardState.CORRECT){


            SlideInWordCard(scale = scale, isVisible = isVisible, currentFlashcard)
        }else{

            WordCard(scale = scale, currentFlashcard)
        }


    }
}

@Composable
fun SlideInWordCard(scale: Float, isVisible: Boolean, currentFlashcard: Flashcard?, ) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { it }, // `it` refers to the width of the composable
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        WordCard(scale, currentFlashcard)
    }
}


@Composable
private fun WordCard(scale: Float, currentFlashcard: Flashcard?) {
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


                AudioIconButton()
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



