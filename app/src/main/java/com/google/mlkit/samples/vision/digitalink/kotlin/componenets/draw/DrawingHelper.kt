package com.google.mlkit.samples.vision.digitalink.kotlin.componenets.draw

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.mlkit.samples.vision.digitalink.PracticeViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.DrawingView
import com.google.mlkit.samples.vision.digitalink.kotlin.StrokeManager
import java.lang.reflect.Modifier


@Composable
fun MyXmlButtonView(strokeManager: StrokeManager, viewModel: PracticeViewModel) {
    
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

