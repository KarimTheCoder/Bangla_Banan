package com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.PracticeUIViewModel


@Composable
fun MyXmlButtonView(strokeManager: StrokeManager, viewModel: PracticeUIViewModel) {
    
    val drawingViewRef = viewModel.drawingViewRef

    AndroidView(
        modifier = androidx.compose.ui.Modifier.height(250.dp).background(Color.LightGray),
        factory = { ctx ->
            DrawingView(ctx).apply {
                setStrokeManager(strokeManager)
                strokeManager.setContentChangedListener(this)

                // Set the DrawingView reference
                drawingViewRef.value = this
            }
        }
    )
}

