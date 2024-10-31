package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.StrokeManager











@Composable
fun RevealTextButton() {
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
                imageVector = Icons.Default.Lock, // Use any icon here
                contentDescription = "Reveal text"
            )
        }

        // Conditionally display the text when isTextVisible is true
        if (isTextVisible) {
            Text(
                text = "Hello, this is the revealed text!",
                modifier = Modifier.padding(top = 8.dp), // Adds some padding at the top
                style = MaterialTheme.typography.bodyMedium // Apply any text style
            )
        }
    }
}
@Composable
fun HorizontalLayoutWithTextButtonAndMatchButton(strokeManager: StrokeManager, viewModel: PracticeViewModel) {
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
            onClick = { viewModel.recognizeClick() },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text("Match")
        }
    }
}