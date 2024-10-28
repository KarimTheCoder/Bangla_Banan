package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.list.ListItemWithTrail

@Composable
fun ResultUI(items: List<String>, onButton1Click: () -> Unit, onButton2Click: () -> Unit) {
    Box(modifier = Modifier.padding(top = 16.dp).fillMaxSize()) {

        // Scrolling list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Padding at the bottom to avoid buttons overlapping content
        ) {
            items(items) { item ->
                ListItemWithTrail(title = item) {

                }
            }
        }

        // Fade effect overlay for the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.TopCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, Color.Transparent)
                    )
                )
        )

        // Fade effect overlay for the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.White)
                    )
                )
        )

        // Fixed buttons at the bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onButton1Click) {
                    Text(text = "Again")
                }
                Button(onClick = onButton2Click) {
                    Text(text = "Next lesson")
                }
            }
        }
    }
}
