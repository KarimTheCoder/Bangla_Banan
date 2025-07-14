package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.model.FlashcardSessionItem

@Composable
fun ResultUI(items: List<FlashcardSessionItem>?, onButton1Click: () -> Unit, onButton2Click: () -> Unit) {


    Box(modifier = Modifier.padding(top = 16.dp).fillMaxSize()) {

        // Scrolling list



        if (items != null) {
            // Display the session items in a LazyColumn (similar to a RecyclerView)
            LazyColumn {
                items(items) { sessionItem ->
                    FlashcardSessionItemView(sessionItem)
                }
            }
        } else {
            Text("No flashcard session items available.")
        }



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
